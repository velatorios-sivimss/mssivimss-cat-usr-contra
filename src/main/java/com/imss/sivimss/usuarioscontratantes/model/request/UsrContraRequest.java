package com.imss.sivimss.usuarioscontratantes.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.imss.sivimss.usuarioscontratantes.model.ContratanteModel;
import com.imss.sivimss.usuarioscontratantes.model.DomicilioModel;

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
	private Integer idLugarNac;
	private String tel;
	private String correo;
	private String calle;
	private String numInt;
	private String numExt;
	private Integer cp;
	private String desEstado;
	private String desMunicipio;
	private String desColonia;


	private Integer idPersona;
	private Integer idDomicilio;
	private Boolean estatus;
//	private ContratanteModel contraModel;
	//private DomicilioModel domicModel;
}
