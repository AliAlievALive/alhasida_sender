package com.alhasid;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TakerTestcontainersTest extends AbstractTakerTestcontainer {

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
