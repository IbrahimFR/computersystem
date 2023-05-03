package com.greenbone.computers.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.greenbone.computers.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComputerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Computer.class);
        Computer computer1 = new Computer();
        computer1.setId(1L);
        Computer computer2 = new Computer();
        computer2.setId(computer1.getId());
        assertThat(computer1).isEqualTo(computer2);
        computer2.setId(2L);
        assertThat(computer1).isNotEqualTo(computer2);
        computer1.setId(null);
        assertThat(computer1).isNotEqualTo(computer2);
    }
}
