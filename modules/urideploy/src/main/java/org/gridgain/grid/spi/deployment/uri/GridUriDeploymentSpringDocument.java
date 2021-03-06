/* 
 Copyright (C) GridGain Systems. All Rights Reserved.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.deployment.uri;

import org.gridgain.grid.compute.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.xml.*;
import java.util.*;

/**
 * Helper class which helps to read deployer and tasks information from
 * {@code Spring} configuration file.
 */
class GridUriDeploymentSpringDocument {
    /** Initialized springs beans factory. */
    private final XmlBeanFactory factory;

    /** List of tasks from GAR description. */
    private List<Class<? extends GridComputeTask<?, ?>>> tasks;

    /**
     * Creates new instance of configuration helper with given configuration.
     *
     * @param factory Configuration factory.
     */
    GridUriDeploymentSpringDocument(XmlBeanFactory factory) {
        assert factory != null;

        this.factory = factory;
    }

    /**
     * Loads tasks declared in configuration by given class loader.
     *
     * @param clsLdr Class loader.
     * @return Declared tasks.
     * @throws GridSpiException Thrown if there are no tasks in
     *      configuration or configuration could not be read.
     */
    @SuppressWarnings({"unchecked"})
    List<Class<? extends GridComputeTask<?, ?>>> getTasks(ClassLoader clsLdr) throws GridSpiException {
        assert clsLdr!= null;

        try {
            if (tasks == null) {
                tasks = new ArrayList<>();

                Map<String, List> beans = factory.getBeansOfType(List.class);

                if (!beans.isEmpty()) {
                    for (List<String> list : beans.values()) {
                        for (String clsName : list) {
                            Class taskCls;

                            try {
                                taskCls = clsLdr.loadClass(clsName);
                            }
                            catch (ClassNotFoundException e) {
                                throw new GridSpiException("Failed to load task class [className=" + clsName + ']', e);
                            }

                            assert taskCls != null;

                            tasks.add(taskCls);
                        }
                    }
                }
            }
        }
        catch (BeansException e) {
            throw new GridSpiException("Failed to get tasks declared in XML file.", e);
        }

        return tasks;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridUriDeploymentSpringDocument.class, this);
    }
}
