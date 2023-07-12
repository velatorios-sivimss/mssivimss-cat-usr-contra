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
public class FiltrosUsrContraRequest {

	private String curp;
	private String nss;
	private String nomContratante;
	private Boolean estatus;
	private Integer id;
	private String tamanio;
	private String pagina;
	
}
