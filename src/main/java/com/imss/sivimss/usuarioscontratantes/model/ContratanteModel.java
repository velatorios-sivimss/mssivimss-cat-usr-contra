package com.imss.sivimss.usuarioscontratantes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.imss.sivimss.usuarioscontratantes.model.request.UsrContraRequest;

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
public class ContratanteModel {

	private Integer idPersona;
	private Integer idDomicilio;
	private Boolean estatus;
}
