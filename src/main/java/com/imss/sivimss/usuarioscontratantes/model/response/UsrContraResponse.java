package com.imss.sivimss.usuarioscontratantes.model.response;




import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UsrContraResponse {
	
	private Integer idContratante;
	private Integer idPersona;
	private Integer idDomicilio;
	private String curp;
	private String telefono;
	private String segundoTel;
	private String correo;
	private String rfc;
	private String fecNacimiento;
	private String nss;
	private String nombre;
	private String paterno;
	private String materno;
	private String cp;
	private String nacionalidad;
	private Integer idPais;
	private String pais;
	private Integer idEstado;
	private String lugarNacimiento;
	private Integer numSexo;
	private String otroSexo;
	private String sexo;
	private String calle;
	private String numExt;
	private String numInt;
	private String colonia;
	private String municipio;
	private String estado;
	private Boolean estatus;
}

