package com.imss.sivimss.usuarioscontratantes.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@JsonIgnoreType(value = true)
public class UsrContraResponse {

	@JsonProperty(value = "curp")
	private String CVE_CURP;
	
	@JsonProperty(value = "telefono")
	private String DES_TELEFONO;
	
	@JsonProperty(value = "rfc")
	private String CVE_RFC;
	
	@JsonProperty(value = "fecNacimiento")
	private String FEC_NAC;
	
	@JsonProperty(value = "nss")
	private String CVE_NSS;
	
	@JsonProperty(value = "nomContratante")
	private String NOMBRE;
	
	@JsonProperty(value = "estatus")
	private Boolean IND_ACTIVO;
}

