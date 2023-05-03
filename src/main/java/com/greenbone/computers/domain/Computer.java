package com.greenbone.computers.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * The Computer entity.
 */
@Schema(description = "The Computer entity.")
@Entity
@Table(name = "computer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Computer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * The macAddress attribute.
     */
    @Schema(description = "The macAddress attribute.")
    @Column(name = "mac_address", nullable = false)
    private String macAddress;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "employee_abbreviation")
    @Size(min = 3, max = 3, message = "Employee abbreviation consists of 3 letters")
    private String employeeAbbreviation;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return this.id;
    }

    public Computer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public Computer macAddress(String macAddress) {
        this.setMacAddress(macAddress);
        return this;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Computer ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmployeeAbbreviation() {
        return this.employeeAbbreviation;
    }

    public Computer employeeAbbreviation(String employeeAbbreviation) {
        this.setEmployeeAbbreviation(employeeAbbreviation);
        return this;
    }

    public void setEmployeeAbbreviation(String employeeAbbreviation) {
        this.employeeAbbreviation = employeeAbbreviation;
    }

    public String getDescription() {
        return this.description;
    }

    public Computer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Computer)) {
            return false;
        }
        return id != null && id.equals(((Computer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Computer{" +
            "id=" + getId() +
            ", macAddress='" + getMacAddress() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", employeeAbbreviation='" + getEmployeeAbbreviation() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
