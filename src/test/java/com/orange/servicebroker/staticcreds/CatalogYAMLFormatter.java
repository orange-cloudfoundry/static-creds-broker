/*
 * *
 *  * Copyright (C) 2015 Orange
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.orange.servicebroker.staticcreds;

import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.tngtech.jgiven.format.ArgumentFormatter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

/**
 * General formatter to format catalog values to yaml.
 */
public class CatalogYAMLFormatter implements ArgumentFormatter<CatalogSettings> {

    @Override
    public String format(CatalogSettings catalogSettings, String... strings) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        final SkipNullEmptyRepresenter representer = new SkipNullEmptyRepresenter();
        representer.addClassTag(CatalogSettings.class, Tag.MAP);

        Yaml yaml = new Yaml(representer, options);
        return yaml.dump(catalogSettings);
    }

    private class SkipNullEmptyRepresenter extends Representer {
        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                                                      Object propertyValue, Tag customTag) {
            NodeTuple tuple = super.representJavaBeanProperty(javaBean, property, propertyValue,
                    customTag);
            Node valueNode = tuple.getValueNode();
            if (Tag.NULL.equals(valueNode.getTag())) {
                return null;// skip 'null' values
            }
            if (valueNode instanceof CollectionNode) {
                if (Tag.SEQ.equals(valueNode.getTag())) {
                    SequenceNode seq = (SequenceNode) valueNode;
                    if (seq.getValue().isEmpty()) {
                        return null;// skip empty lists
                    }
                }
                if (Tag.MAP.equals(valueNode.getTag())) {
                    MappingNode seq = (MappingNode) valueNode;
                    if (seq.getValue().isEmpty()) {
                        return null;// skip empty maps
                    }
                }
            }
            return tuple;
        }
    }
}