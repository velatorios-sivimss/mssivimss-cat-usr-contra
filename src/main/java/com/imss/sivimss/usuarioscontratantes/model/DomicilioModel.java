package com.imss.sivimss.usuarioscontratantes.model;

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
public class DomicilioModel {
	
	private String calle;
	private String numInt;
	private String numExt;
	private Integer cp;
	private String desEstado;
	private String desMunicipio;
	private String desColonia;

}
