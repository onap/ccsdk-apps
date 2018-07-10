/*
 * Copyright © 2015 ZTE and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.highstreet.technologies.odl.app.impl.delegates;

import java.util.HashMap;

/**
 * Created by odl on 17-6-5.
 */
public class PredefinePath
{
    public HashMap<String, Path> paths;

    @Override
    public String toString()
    {
        return "PredefinePath{" +
                "paths=" + paths +
                '}';
    }
}
