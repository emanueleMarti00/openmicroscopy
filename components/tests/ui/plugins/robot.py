#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# Copyright (C) 2014 University of Dundee & Open Microscopy Environment.
# All rights reserved.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

from omero.cli import BaseControl, CLI
import sys
import os

HELP = "Robot framework configuration/deployment tools"


class RobotControl(BaseControl):

    def _configure(self, parser):
        sub = parser.sub()

        config = parser.add(
            sub, self.config,
            "Output a config template for the Robot framework")
        config.add_argument(
            "--config-file", type=str,
            help="Path to an ICE configuration file. Default: ICE_CONFIG")

    def config(self, args):

        if args.config_file:
            init_args = ["--Ice.Config=%s" % args.config_file]
        else:
            init_args = ["--Ice.Config=%s" % os.environ.get('ICE_CONFIG', '')]
        import Ice
        p = Ice.initialize(init_args).getProperties()

        # Create dictionary of substitutions from ice.config file
        d = {
            "PORT": p.getPropertyWithDefault("omero.port", '4064'),
            "HOST": p.getPropertyWithDefault("omero.host", "localhost"),
            "USER": p.getPropertyWithDefault("omero.user", "root"),
            "PASS": p.getPropertyWithDefault("omero.pass", "omero"),
            "WEBPORT": '80',
            "ENCODED_WEBPREFIX": 'test',
            "ROOTPASS": p.getPropertyWithDefault("omero.rootpass", "omero"),
        }

        # Add OMERO.web substitutions
        import urllib
        from omeroweb import settings
        static_prefix = getattr(settings, 'FORCE_SCRIPT_NAME', '')
        d["WEBPREFIX"] = static_prefix
        d["QWEBPREFIX"] = urllib.quote(static_prefix, '')
        d["QSEP"] = urllib.quote('/', '')

        # Read robot.template file and substitute keywords
        c = file(self.ctx.dir / "etc" / "robot.template").read()
        self.ctx.out(c % d)


try:
    register("robot", RobotControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("robot", RobotControl, HELP)
        cli.invoke(sys.argv[1:])
