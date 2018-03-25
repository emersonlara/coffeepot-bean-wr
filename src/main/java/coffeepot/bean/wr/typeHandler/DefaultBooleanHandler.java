/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import coffeepot.bean.wr.mapper.Metadata;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 Jeandeson O. Merelis
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultBooleanHandler implements TypeHandler<Boolean> {

    private String trueText;
    private String falseText;

    protected static String trueTextDefault = "true";
    protected static String falseTextDefault = "false";



    @Override
    public Boolean parse(String text, Metadata metadata) throws HandlerParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        if (text.equals(trueText)) {
            return Boolean.TRUE;
        }

        if (text.equals(falseText)) {
            return Boolean.FALSE;
        }

        throw new HandlerParseException("Can not convert the text \"" + text + "\" to Boolean");
    }

    @Override
    public String toString(Boolean obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }

        return obj == true ? trueText : falseText;
    }

    /**
     * When keys value are not specified, the sequence of values ​​must be first
     * true text and second false text.
     * <p></p>
     * Valid params format:<br>
     * true=1;false=0 or false=0;true=1<br>
     * 1;0<br>
     * newTrueText;newFalseText
     *
     */
    @Override
    public void setConfig(String[] params) {
        if (params == null || params.length == 0) {
            setDefaultValues();
            return;
        }
        String trueTxt = null;
        String falseTxt = null;

        for (String s : params) {
            String[] keyValue = s.split(";");
            if (keyValue.length > 0) {
                String key = keyValue[0].trim();
                String[] keys = key.split("=");

                if (keys.length > 1) {

                    //format true=1;false=0
                    String k = keys[0].trim();
                    if ("true".equals(k)) {
                        trueTxt = keys[1].trim();
                    } else if ("false".equals(k)) {
                        falseTxt = keys[1].trim();
                    }

                    if (keyValue.length > 1) {
                        key = keyValue[1].trim();
                        keys = key.split("=");
                        if (keys.length > 1) {
                            k = keys[0].trim();
                            if ("true".equals(k)) {
                                trueTxt = keys[1].trim();
                            } else if ("false".equals(k)) {
                                falseTxt = keys[1].trim();
                            }
                        }
                    }

                } else {

                    // format trueText;falseText
                    trueTxt = keyValue[0].trim();
                    if (keyValue.length > 1) {
                        falseTxt = keyValue[1].trim();
                    }
                }

            }
        }
        if (trueTxt != null && falseTxt != null && !trueTxt.equals(falseTxt)) {
            trueText = trueTxt;
            falseText = falseTxt;
        }
    }

    private void setDefaultValues() {
        trueText = trueTextDefault;
        falseText = falseTextDefault;
    }

    public static String getTrueTextDefault() {
        return trueTextDefault;
    }

    public static void setTrueTextDefault(String trueTextDefault) {
        DefaultBooleanHandler.trueTextDefault = trueTextDefault;
    }

    public static String getFalseTextDefault() {
        return falseTextDefault;
    }

    public static void setFalseTextDefault(String falseTextDefault) {
        DefaultBooleanHandler.falseTextDefault = falseTextDefault;
    }


}
