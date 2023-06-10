package com.imss.sivimss.usuarioscontratantes.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class UsrContraRequest {

	private Integer idContratante;
	private Integer idPersona;
	private Integer idDomicilio;
	private String curp;
	private String nss;
	private String nombre;
	private String paterno;
	private String materno;
	private String rfc;
	private Integer numSexo;
	private String otroSexo;
	private String fecNacimiento;
	private Integer idPais;
	private Integer idlugarNac;
	private String tel;
	private String correo;
	private String calle;
	private String numExte;
	private String numInt;
	private Integer cp;
	private String desColonia;
	private String desMunicpio;
	private String desEstado;
	private Boolean estatus;
	
	
}
