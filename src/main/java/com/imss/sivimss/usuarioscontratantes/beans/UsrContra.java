package com.imss.sivimss.usuarioscontratantes.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.usuarioscontratantes.model.ContratanteModel;
import com.imss.sivimss.usuarioscontratantes.model.DomicilioModel;
import com.imss.sivimss.usuarioscontratantes.model.request.FiltrosUsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.UsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.util.AppConstantes;
import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.QueryHelper;
import com.imss.sivimss.usuarioscontratantes.util.SelectQueryUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UsrContra {
	
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
	private Integer idUsuario;
	private Boolean estatus;
	
	public UsrContra(UsrContraRequest contraR) {
		this.idContratante = contraR.getIdContratante();
		this.curp = contraR.getCurp();
		this.nss = contraR.getNss();
		this.nombre = contraR.getNombre();
		this.paterno = contraR.getPaterno();
		this.materno = contraR.getMaterno();
		this.rfc = contraR.getRfc();
		this.numSexo = contraR.getNumSexo();
		this.otroSexo = contraR.getOtroSexo();
		this.fecNacimiento = contraR.getFecNacimiento();
		this.idPais = contraR.getIdPais();
		this.idlugarNac = contraR.getIdLugarNac();
		this.tel = contraR.getTel();
		this.correo = contraR.getCorreo();
		this.calle = contraR.getCalle();
		this.numExte = contraR.getNumExt();
		this.numInt = contraR.getNumInt();
		this.cp = contraR.getCp();
		this.desColonia = contraR.getDesColonia();
		this.desMunicpio = contraR.getDesMunicipio();
		this.desEstado = contraR.getDesEstado();
		this.estatus = contraR.getEstatus();
		this.idDomicilio = contraR.getIdDomicilio();
		this.idPersona=contraR.getIdPersona();
	}

	
	public DatosRequest buscarContratantes(DatosRequest request, FiltrosUsrContraRequest filtros) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("SC.ID_CONTRATANTE",
				"SP.CVE_CURP AS curp",
				"SP.CVE_NSS AS NSS",
				"CONCAT(SP.NOM_PERSONA, ' ',"
				+ "SP.NOM_PRIMER_APELLIDO, ' ',"
				+ "SP.NOM_SEGUNDO_APELLIDO) AS NomContratante",
				"SP.CVE_RFC AS rfc",
				"DATE_FORMAT(SP.FEC_NAC, '%d-%m-%Y') AS fecNacimiento",
				"SP.DES_TELEFONO AS tel",
				"SC.IND_ACTIVO AS estatus")
		.from("SVC_CONTRATANTE SC")
		.join("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA");
		if(filtros.getCurp()!=null && filtros.getNss()==null && filtros.getNomContratante()==null && filtros.getEstatus()==null) {
			queryUtil.where("SP.CVE_CURP= '"+filtros.getCurp()+"'");
		}else if(filtros.getCurp()==null && filtros.getNss()!=null && filtros.getNomContratante()==null && filtros.getEstatus()==null) {
			queryUtil.where("SP.CVE_NSS= '"+filtros.getNss()+"'");
			}else if(filtros.getCurp()==null && filtros.getNss()==null && filtros.getNomContratante()!=null && filtros.getEstatus()==null) {
			queryUtil.where("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'");
		}else if(filtros.getCurp()==null && filtros.getNss()==null && filtros.getNomContratante()==null && filtros.getEstatus()!=null) {
			queryUtil.where("SC.IND_ACTIVO= "+filtros.getEstatus()+"");
		}else if(filtros.getCurp()!=null && filtros.getNss()!=null && filtros.getNomContratante()==null && filtros.getEstatus()==null) {
			queryUtil.where("SP.CVE_CURP=:curp").and("SP.CVE_NSS=:nss")
			.setParameter("curp", filtros.getCurp())
			.setParameter("nss", filtros.getNss());
		}else if(filtros.getCurp()!=null && filtros.getNss()==null && filtros.getNomContratante()!=null && filtros.getEstatus()==null) {
			queryUtil.where("SP.CVE_CURP=:curp").and("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'")
			.setParameter("curp", filtros.getCurp());
		}else if(filtros.getCurp()!=null && filtros.getNss()==null && filtros.getNomContratante()==null && filtros.getEstatus()!=null) {
			queryUtil.where("SP.CVE_CURP=:curp").and("SC.IND_ACTIVO=:estatus")
			.setParameter("curp", filtros.getCurp())
			.setParameter("estatus", filtros.getEstatus());
		}else if(filtros.getCurp()!=null && filtros.getNss()!=null && filtros.getNomContratante()!=null && filtros.getEstatus()==null) {
			queryUtil.where("SP.CVE_CURP=:curp").and("SP.CVE_NSS=:nss").and("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'")
			.setParameter("curp", filtros.getCurp())
			.setParameter("nss", filtros.getNss());
		}else if(filtros.getCurp()!=null && filtros.getNss()!=null && filtros.getNomContratante()==null && filtros.getEstatus()!=null) {
			queryUtil.where("SP.CVE_CURP=:curp").and("SP.CVE_NSS=:nss").and("SC.IND_ACTIVO=:estatus")
			.setParameter("curp", filtros.getCurp())
			.setParameter("nss", filtros.getNss())
			.setParameter("estatus", filtros.getEstatus());
		}else if(filtros.getCurp()==null && filtros.getNss()!=null && filtros.getNomContratante()!=null && filtros.getEstatus()!=null) {
			queryUtil.where("SP.CVE_NSS=:nss").and("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'").and("SC.IND_ACTIVO=:estatus")
			.setParameter("nss", filtros.getNss())
			.setParameter("estatus", filtros.getEstatus());
		}else if(filtros.getCurp()==null && filtros.getNss()!=null && filtros.getNomContratante()==null && filtros.getEstatus()!=null) {
			queryUtil.where("SP.CVE_NSS=:nss").and("SC.IND_ACTIVO=:estatus")
			.setParameter("nss", filtros.getNss())
			.setParameter("estatus", filtros.getEstatus());
		}else if(filtros.getCurp()==null && filtros.getNss()!=null && filtros.getNomContratante()!=null && filtros.getEstatus()==null) {
			queryUtil.where("SP.CVE_NSS=:nss").and("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'")
			.setParameter("nss", filtros.getNss());
		}else if(filtros.getCurp()==null && filtros.getNss()==null && filtros.getNomContratante()!=null && filtros.getEstatus()!=null) {
			queryUtil.where("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'").and("SC.IND_ACTIVO=:estatus")
			.setParameter("estatus", filtros.getEstatus());
		}else if(filtros.getCurp()!=null && filtros.getNss()!=null && filtros.getNomContratante()!=null && filtros.getEstatus()!=null) {
			queryUtil.where("SP.CVE_CURP=:curp").and("SP.CVE_NSS=:nss").and("SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'").and("SC.IND_ACTIVO=:estatus")
			.setParameter("curp", filtros.getCurp())
			.setParameter("nss", filtros.getNss())
			.setParameter("estatus", filtros.getEstatus());
		}
		String query = obtieneQuery(queryUtil);
		log.info("-> " +query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
	    parametros.put("pagina",filtros.getPagina());
        parametros.put("tamanio",filtros.getTamanio());
        request.getDatos().remove("datos");
	    request.setDatos(parametros);
		return request;
	}
	
	private static String obtieneQuery(SelectQueryUtil queryUtil) {
        return queryUtil.build();
    }
	
	private static String encodedQuery(String query) {
        return DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
    }

	public DatosRequest verDetalle(DatosRequest request) {
		Map<String, Object> parametros = new HashMap<>();
		String palabra = request.getDatos().get("palabra").toString();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("SC.ID_CONTRATANTE",
				"SP.CVE_CURP",
				"SP.CVE_NSS",
				"SP.NOM_PERSONA",
				"SP.NOM_PRIMER_APELLIDO",
			    "SP.NOM_SEGUNDO_APELLIDO",
				"SP.CVE_RFC",
				"SP.NUM_SEXO",
				"DATE_FORMAT(SP.FEC_NAC, '%d/%m/%Y')",
				"SP.ID_PAIS",
				"SPA.DES_PAIS",
				"CASE "
				+ "WHEN SP.ID_PAIS=119 THEN 'MEXICANA' "
				+ "ELSE 'EXTRANJERA' END "
				+ "AS NACIONALIDAD",
				"SP.ID_ESTADO",
				"SE.DES_ESTADO",
				"SP.DES_TELEFONO",
				"SP.DES_CORREO",
				"SD.DES_CALLE",
				"SD.NUM_EXTERIOR",
				"SD.NUM_INTERIOR",
				"SD.DES_CP",
				"CP.DES_COLONIA",
				"CP.DES_MNPIO",
				"CP.DES_ESTADO",
				"SC.IND_ACTIVO")
		.from("SVC_CONTRATANTE SC")
		.join("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
		.join("SVC_PAIS SPA", "SP.ID_PAIS = SPA.ID_PAIS")
		.join("SVC_ESTADO SE", "SP.ID_ESTADO = SE.ID_ESTADO")
		.join("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO")
		.leftJoin("SVC_CP CP", "SD.DES_CP = CP.CVE_CODIGO_POSTAL");
		queryUtil.where("SC.ID_CONTRATANTE= :id")
		.setParameter("id", Integer.parseInt(palabra));
		String query = obtieneQuery(queryUtil);
		log.info("-> " +query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
	    request.setDatos(parametros);
	    return request;
	}


	public DatosRequest insertarPersona() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_PERSONA");
		q.agregarParametroValues(" NOM_PERSONA", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + this.materno + "'");
		q.agregarParametroValues("FEC_NAC", "'" + this.fecNacimiento + "'");
		q.agregarParametroValues("CVE_CURP", "'"+ this.curp + "'");
		q.agregarParametroValues("CVE_RFC", "'" +this.rfc +"'");
		q.agregarParametroValues("ID_PAIS", "'" + this.idPais + "'");
		q.agregarParametroValues("ID_ESTADO", "'"+ this.idlugarNac+ "'");
		q.agregarParametroValues("DES_CORREO", "'"+ this.correo +"'");
		q.agregarParametroValues("DES_TELEFONO", "'" + this.tel + "'");
		q.agregarParametroValues("ID_USUARIO_ALTA", ""+idUsuario+"");
		q.agregarParametroValues("FEC_ALTA", ""+AppConstantes.CURRENT_TIMESTAMP+"");
		String query = q.obtenerQueryInsertar()+"$$"  + insertarDomic(); //+"$$"+ insertarContra();
		log.info(query);
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		        parametro.put(AppConstantes.QUERY, encoded);
		        parametro.put("separador","$$");
		        parametro.put("replace","idTabla");
		       // parametro.put("replace","idTabla2");
		        request.setDatos(parametro);
		return request;
	}


	private String insertarContra() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", "idTabla");
		q.agregarParametroValues("ID_DOMICILIO", "idTabla");
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("ID_USUARIO_ALTA", ""+idUsuario+"");
		q.agregarParametroValues("FEC_ALTA", ""+AppConstantes.CURRENT_TIMESTAMP+"");
		String query = q.obtenerQueryInsertar();
	
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		        parametro.put(AppConstantes.QUERY, encoded);
		        request.setDatos(parametro);
		        return query;
	}


	private String insertarDomic() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'" + this.calle+ "'");
		q.agregarParametroValues("NUM_EXTERIOR", "'" + this.numExte + "'");
		q.agregarParametroValues("NUM_INTERIOR", "'" + this.numInt + "'");
		q.agregarParametroValues("DES_CP", "" + this.cp + "");
		q.agregarParametroValues("DES_COLONIA", "'"+this.desColonia + "'");
		q.agregarParametroValues("DES_MUNICIPIO", "'"+this.desMunicpio+"'");
		q.agregarParametroValues("DES_ESTADO", "'"+ this.desEstado +"'");
		q.agregarParametroValues("ID_USUARIO_ALTA", ""+idUsuario+"");
		q.agregarParametroValues("FEC_ALTA", ""+AppConstantes.CURRENT_TIMESTAMP+"");
		String query = q.obtenerQueryInsertar(); //+"$$"+ insertarContra();
		log.info(query);
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		        parametro.put(AppConstantes.QUERY, encoded);
		    parametro.put("separador","$$");
		        parametro.put("replace","idTabla");
		        request.setDatos(parametro);
		        return query;
	}


	public DatosRequest editarPersona() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVC_PERSONA");
		q.agregarParametroValues("NOM_PERSONA", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + this.materno + "'");
		q.agregarParametroValues("CVE_RFC", "'" +this.rfc +"'");
		q.agregarParametroValues("NUM_SEXO", "" +this.numSexo +"");
		
			q.agregarParametroValues("DES_OTRO_SEXO", "'" +this.otroSexo +"'");	
		
		q.agregarParametroValues("FEC_NAC", "'" + this.fecNacimiento + "'");
		q.agregarParametroValues("ID_PAIS", "" + this.idPais + "");
		q.agregarParametroValues("ID_ESTADO", ""+ this.idlugarNac+ "");
		q.agregarParametroValues("DES_TELEFONO", "'" + this.tel + "'");
		q.agregarParametroValues("DES_CORREO", "'"+ this.correo +"'");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", ""+idUsuario+"");
		q.agregarParametroValues("FEC_ACTUALIZACION", ""+AppConstantes.CURRENT_TIMESTAMP+"");
		q.addWhere("ID_PERSONA= " +this.idPersona);
		String query = q.obtenerQueryActualizar(); //"$$" + editarDomic();
		log.info(query);
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		        parametro.put(AppConstantes.QUERY, encoded);
		         parametro.put("separador","$$");
		        request.setDatos(parametro);
		return request;
	}


	public DatosRequest  editarDomic() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'" + this.calle+ "'");
		q.agregarParametroValues("NUM_EXTERIOR", "'" + this.numExte + "'");
		q.agregarParametroValues("NUM_INTERIOR", "'" + this.numInt + "'");
		q.agregarParametroValues("DES_CP", "" +this.cp +"");
		if(this.desColonia!=null || this.desMunicpio!=null) {
			q.agregarParametroValues("DES_COLONIA", "'"+this.desColonia + "'");
			q.agregarParametroValues("DES_MUNICIPIO", "'"+this.desMunicpio+"'");
			q.agregarParametroValues("DES_ESTADO", "'"+ this.desEstado +"'");
		}
		q.agregarParametroValues("ID_USUARIO_MODIFICA", ""+idUsuario+"");
		q.agregarParametroValues("FEC_ACTUALIZACION", ""+AppConstantes.CURRENT_TIMESTAMP+"");
		q.addWhere("ID_DOMICILIO= " +this.idDomicilio);
		String query =q.obtenerQueryActualizar();
		log.info(query);
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}


	
}
