/**
 * Copyright 2018 The CloudEvents Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudevents;

import io.cloudevents.util.JacksonMapper;
import org.junit.Test;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

public class CloudEventJacksonTest {

    @Test
    public void testParseAzureJSON() {
        CloudEvent<Map<String, ?>> ce = JacksonMapper.fromInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("azure.json"));
        assertThat(ce.getEventType()).isEqualTo("Microsoft.Storage.BlobCreated");

        ce.getData().ifPresent(data -> {
            assertThat(Map.class).isAssignableFrom(data.getClass());
            assertThat(data.get("clientRequestId")).isEqualTo("a23b4aba-2755-4107-8020-8ba6c54b203d");
            assertThat(Map.class).isAssignableFrom(data.get("storageDiagnostics").getClass());
            Map<String, String> storageDiagnostics = (Map<String, String>) data.get("storageDiagnostics");
            assertThat(storageDiagnostics).containsOnlyKeys("batchId");
            assertThat(storageDiagnostics.get("batchId")).isEqualTo("ba4fb664-f289-4742-8067-6c859411b066");
        });



    }

    @Test
    public void testParseAmazonJSON() {
        CloudEvent ce = JacksonMapper.fromInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("aws.json"));
        assertThat(ce.getEventType()).isEqualTo("aws.s3.object.created");
        assertThat(ce.getSource().equals(URI.create("https://serverless.com")));
        assertThat(ce.getEventTime().get()).isEqualTo(ZonedDateTime.parse("2018-04-26T14:48:09.769Z", ISO_ZONED_DATE_TIME));
    }
}