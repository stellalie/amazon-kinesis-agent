/*
 * Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License. 
 * A copy of the License is located at
 * 
 *  http://aws.amazon.com/asl/
 *  
 * or in the "license" file accompanying this file. 
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.amazon.kinesis.streaming.agent.processing.processors;

import com.amazon.kinesis.streaming.agent.ByteBuffers;
import com.amazon.kinesis.streaming.agent.processing.exceptions.DataConversionException;
import com.amazon.kinesis.streaming.agent.processing.interfaces.IDataConverter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Replace the newline with a whitespace
 * Remove leading and trailing spaces for each line
 * 
 * Configuration looks like:
 * 
 * { "optionName": "SINGLELINE" }
 * 
 * @author chaocheq
 *
 */
public class LearnosityDataConverter implements IDataConverter {

    @Override
    public ByteBuffer convert(ByteBuffer data) throws DataConversionException {
        String dataStr = ByteBuffers.toString(data, StandardCharsets.UTF_8);

        // Preserve the NEW_LINE at the end of the JSON record
        if (dataStr.endsWith(NEW_LINE)) {
            dataStr = dataStr.substring(0, (dataStr.length() - NEW_LINE.length()));
        }

        // TODO: Stupid hack here since im too lazy to think of proper solution
        String[] array = dataStr.split(" -  ", -1);
        String dataRes = array[1].trim() + NEW_LINE;

        // TODO: Replace "value" because it cant exists
        dataRes = dataRes.replace("\"value\"", "\"metric_value\"");

        return ByteBuffer.wrap(dataRes.getBytes(StandardCharsets.UTF_8));
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
