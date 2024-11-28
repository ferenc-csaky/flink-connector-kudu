package org.apache.flink.connector.kudu.table;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

/** Common options for Kudu tables and catalogs. */
@PublicEvolving
public class KuduCommonOptions {

    public static final ConfigOption<String> KUDU_MASTERS =
            ConfigOptions.key("kudu.masters")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("kudu's master server address");
}
