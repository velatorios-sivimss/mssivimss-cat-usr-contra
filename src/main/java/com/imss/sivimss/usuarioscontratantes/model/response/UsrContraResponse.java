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
	
	@JsonProperty(value = "idContratante")
	private Integer ID_CONTRATANTE;
	
	@JsonProperty(value = "idPersona")
	private Integer ID_PERSONA;
	
	@JsonProperty(value = "idDomicilio")
	private Integer ID_DOMICILIO;

	@JsonProperty(value = "curp")
	private String CVE_CURP;
	
	@JsonProperty(value = "telefono")
	private String DES_TELEFONO;
	
	@JsonProperty(value = "correo")
	private String DES_CORREO;
	
	@JsonProperty(value = "rfc")
	private String CVE_RFC;
	
	@JsonProperty(value = "fecNacimiento")
	private String FEC_NAC;
	
	@JsonProperty(value = "nss")
	private String CVE_NSS;
	
	@JsonProperty(value = "nombre")
	private String NOM_PERSONA;
	
	@JsonProperty(value = "paterno")
	private String NOM_PRIMER_APELLIDO;
	
	@JsonProperty(value = "materno")
	private String NOM_SEGUNDO_APELLIDO;
	
	@JsonProperty(value = "cp")
	private Integer DES_CP;
	
	@JsonProperty(value = "nacionalidad")
	private String NACIONALIDAD;
	
	@JsonProperty(value = "idPais")
	private Integer ID_PAIS;
	
	@JsonProperty(value = "pais")
	private String PAIS;
	
	@JsonProperty(value = "idEstado")
	private Integer ID_ESTADO;
	
	@JsonProperty(value = "lugarNacimiento")
	private String LUGAR_NACIMIENTO;
	
	@JsonProperty(value = "numSexo")
	private Integer NUM_SEXO;
	
	@JsonProperty(value = "desOtroSexo")
	private String DES_OTRO_SEXO;
	
	@JsonProperty(value = "sexo")
	private String SEXO;
	
	@JsonProperty(value = "calle")
	private String DES_CALLE;
	
	@JsonProperty(value = "numExt")
	private String NUM_EXTERIOR;
	
	@JsonProperty(value = "numInt")
	private String NUM_INTERIOR;
	
	@JsonProperty(value = "colonia")
	private String DES_COLONIA;
	
	@JsonProperty(value = "municipio")
	private String DES_MUNPIO;
	
	@JsonProperty(value = "estado")
	private String DES_ESTADO;
	
	
	
	@JsonProperty(value = "estatus")
	private Boolean IND_ACTIVO;
}

