// **********************************************************************
//
// Copyright (c) 2003-2005 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 2.1.2

package mono;

public interface TPrx extends Ice.ObjectPrx
{
    public ome.model.roi.Roi5DRemote getRoi5D();
    public ome.model.roi.Roi5DRemote getRoi5D(java.util.Map __ctx);
}
