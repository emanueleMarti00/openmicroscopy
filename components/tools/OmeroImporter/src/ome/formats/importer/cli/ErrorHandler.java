/*
 *   $Id$
 *
 *   Copyright 2008 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */

package ome.formats.importer.cli;

import static ome.formats.importer.ImportEvent.*;
import ome.formats.importer.IObservable;
import ome.formats.importer.IObserver;
import ome.formats.importer.ImportConfig;
import ome.formats.importer.ImportEvent;
import ome.formats.importer.ImportLibrary;
import ome.formats.importer.ImportCandidates.SCANNING_FILE_EXCEPTION;
import ome.formats.importer.util.ErrorContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link IObserver} based on the gui ErrorHandler code which collects all
 * exceptions during
 * {@link ImportLibrary#importCandidates(ome.formats.importer.ImportConfig, ome.formats.importer.ImportCandidates)}
 * and after the import is finished offers to submit them via the feedback
 * system.
 *
 * @since Beta4.1
 */
public class ErrorHandler extends ome.formats.importer.util.ErrorHandler {

    private final static Log log = LogFactory.getLog(ErrorHandler.class);

    public ErrorHandler(ImportConfig config) {
        super(config);
    }

    public void onUpdate(IObservable importLibrary, ImportEvent event) {

        if (event instanceof IMPORT_DONE) {
            log.info("Number of errors: " + errors.size());
        }

        else if (event instanceof FILE_EXCEPTION) {
            FILE_EXCEPTION ev = (FILE_EXCEPTION) event;
            log.error(ev.toLog());
        }

        else if (event instanceof SCANNING_FILE_EXCEPTION) {
            SCANNING_FILE_EXCEPTION ev = (SCANNING_FILE_EXCEPTION) event;
            log.debug(ev.toLog(), ev.exception);
        }

        else if (event instanceof EXCEPTION_EVENT) {
            EXCEPTION_EVENT ev = (EXCEPTION_EVENT) event;
            log.debug(ev.toLog(), ev.exception);
        }

        else if (event instanceof ImportEvent.DEBUG_SEND) {

            for (ErrorContainer error : errors) {
                error.setEmail(config.email.get());
                error.setComment("Sent from CLI");
            }

            if (errors.size() > 0) {

                sendFiles = ((ImportEvent.DEBUG_SEND) event).sendFiles;
                log.info("Sending error report "
                    + ( sendFiles ? "with files " : " ") + "...");
                sendErrors();
            }

        }

    }

}
