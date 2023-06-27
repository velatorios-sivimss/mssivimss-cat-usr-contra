package com.imss.sivimss.usuarioscontratantes.beans;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.usuarioscontratantes.model.request.FiltrosUsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.ReporteDto;
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

	//Tablas
	public static final String SVC_PERSONA = "SVC_PERSONA SP";
	public static final String SVC_CONTRATANTE = "SVC_CONTRATANTE SC";
	
	//Parameters
	public static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	public static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	public static final String ID_USUARIO_ALTA = "ID_USUARIO_ALTA";
	public static final String FEC_ALTA = "FEC_ALTA";
	public static final String IND_ACTIVO = "IND_ACTIVO";
	public static final String ID_TABLA = "idTabla";
	
	//JOIN
	public static final String SC_ID_PERSONA_SP_ID_PERSONA = "SC.ID_PERSONA = SP.ID_PERSONA";
	
	public DatosRequest buscarContratantes(DatosRequest request, FiltrosUsrContraRequest filtros, String fecFormat) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("SC.ID_CONTRATANTE AS idContratante",
				"SC.ID_DOMICILIO AS idDomicilio",
				"SC.ID_PERSONA AS idPersona",
				"SP.CVE_CURP AS curp",
				"SP.CVE_NSS AS nss",
				"CONCAT(SP.NOM_PERSONA, ' ',"
				+ "SP.NOM_PRIMER_APELLIDO, ' ',"
				+ "SP.NOM_SEGUNDO_APELLIDO) AS nomContratante",
				"SP.CVE_RFC AS rfc",
				"DATE_FORMAT(SP.FEC_NAC, '"+fecFormat+"') AS fecNacimiento",
				"SP.DES_TELEFONO AS tel",
				"SC.IND_ACTIVO AS estatus")
		.from(SVC_CONTRATANTE)
		.join(SVC_PERSONA, SC_ID_PERSONA_SP_ID_PERSONA);
		StringBuilder where= new StringBuilder();
		if(filtros.getCurp()!=null) {
			where.append(" AND SP.CVE_CURP= '"+filtros.getCurp()+"'");
		}
	    if(filtros.getNss()!=null) {
			where.append(" AND SP.CVE_NSS= '"+filtros.getNss()+"'");
		}
	    if (filtros.getNomContratante()!=null) {
			where.append(" AND SP.NOM_PERSONA LIKE '%"+filtros.getNomContratante()+"%'");
		} 
	    if(filtros.getEstatus()!=null) {
			where.append(" AND SC.IND_ACTIVO= "+filtros.getEstatus()+"");
		}
	    if(where.toString().contains("AND")) {
	    	queryUtil.where(where.toString().replaceFirst("AND", ""));
	    }String query = obtieneQuery(queryUtil);
		log.info("-> " +query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
	    parametros.put("pagina",filtros.getPagina());
        parametros.put("tamanio",filtros.getTamanio());
        request.getDatos().remove(AppConstantes.DATOS);
	    request.setDatos(parametros);
		return request;
	}

	public DatosRequest verDetalle(DatosRequest request, String fecFormat) {
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
				"DATE_FORMAT(SP.FEC_NAC, '"+fecFormat+"')",
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
				"IFNULL(CP.DES_COLONIA, SD.DES_COLONIA) AS DES_COLONIA",
				"IFNULL(CP.DES_ESTADO, SD.DES_ESTADO) AS DES_ESTADO",
				"IFNULL(CP.DES_MNPIO, SD.DES_MUNICIPIO) AS DES_MUNPIO",
				"SC.IND_ACTIVO")
		.from(SVC_CONTRATANTE)
		.join(SVC_PERSONA, SC_ID_PERSONA_SP_ID_PERSONA)
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
		q.agregarParametroValues(ID_USUARIO_ALTA, ""+idUsuario+"");
		q.agregarParametroValues(FEC_ALTA, ""+AppConstantes.CURRENT_TIMESTAMP+"");
		String query = q.obtenerQueryInsertar() +"$$"+ insertarContra();
		String encoded = encodedQuery(query);      
		        parametro.put(AppConstantes.QUERY, encoded);
		        parametro.put("separador","$$");
		        parametro.put("replace", ID_TABLA);
		        request.setDatos(parametro);
		return request;
	}
	
	public DatosRequest insertarDomic() {
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
		q.agregarParametroValues(ID_USUARIO_ALTA, ""+idUsuario+"");
		q.agregarParametroValues(FEC_ALTA, ""+AppConstantes.CURRENT_TIMESTAMP+"");
		String query = q.obtenerQueryInsertar() +"$$" +actualizarContra(idUsuario); 
		log.info(query);
		String encoded = encodedQuery(query);
		        parametro.put(AppConstantes.QUERY, encoded);
		        parametro.put("separador","$$");
		        parametro.put("replace", ID_TABLA);
		        request.setDatos(parametro);
		        return request;
	}
	

	private String actualizarContra(Integer idUsuario) {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVC_CONTRATANTE ");
		q.agregarParametroValues("CVE_MATRICULA", "NULL");
		q.agregarParametroValues("ID_DOMICILIO", ID_TABLA);
		q.agregarParametroValues(IND_ACTIVO, "1");
		q.agregarParametroValues(ID_USUARIO_ALTA, ""+idUsuario+"");
		q.agregarParametroValues(FEC_ALTA, ""+AppConstantes.CURRENT_TIMESTAMP+"") ;
		q.addWhere("IND_ACTIVO=0 AND CVE_MATRICULA='idDomicilio'");
		String query = q.obtenerQueryActualizar().replace("coalesce(NULL,CVE_MATRICULA)", "NULL");
		log.info("---> "+query);
		String encoded = encodedQuery(query);
		        parametro.put(AppConstantes.QUERY, encoded);
		        request.setDatos(parametro);
		        return query;
	}

	private String insertarContra() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", ID_TABLA);
		q.agregarParametroValues("ID_DOMICILIO", "1");
		q.agregarParametroValues(IND_ACTIVO, "0");
		q.agregarParametroValues(ID_USUARIO_ALTA, "1");
		q.agregarParametroValues("CVE_MATRICULA", "'idDomicilio'");
		String query = q.obtenerQueryInsertar();
		String encoded = encodedQuery(query);
		        parametro.put(AppConstantes.QUERY, encoded);
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
		if(this.otroSexo!=null) {
			q.agregarParametroValues("DES_OTRO_SEXO", "'" +this.otroSexo +"'");		
		}
		q.agregarParametroValues("FEC_NAC", "'"+fecNacimiento+"'");
		q.agregarParametroValues("ID_PAIS", "" + this.idPais + "");
		q.agregarParametroValues("ID_ESTADO", ""+ this.idlugarNac+ "");
		q.agregarParametroValues("DES_TELEFONO", "'" + this.tel + "'");
		q.agregarParametroValues("DES_CORREO", "'"+ this.correo +"'");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, ""+idUsuario+"");
		q.agregarParametroValues(FEC_ACTUALIZACION, ""+AppConstantes.CURRENT_TIMESTAMP+"");
		q.addWhere("ID_PERSONA= " +this.idPersona);
		String query = q.obtenerQueryActualizar();
		log.info(query);
		String encoded = encodedQuery(query);
		        parametro.put(AppConstantes.QUERY, encoded);
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
		q.agregarParametroValues(ID_USUARIO_MODIFICA, ""+idUsuario+"");
		q.agregarParametroValues(FEC_ACTUALIZACION, ""+AppConstantes.CURRENT_TIMESTAMP+"");
		q.addWhere("ID_DOMICILIO= " +this.idDomicilio);
		String query =q.obtenerQueryActualizar();
		log.info(query);
		String encoded = encodedQuery(query);
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}


	public DatosRequest cambiarEstatus(Boolean estatus, Integer idContratante) {
		DatosRequest request = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVC_CONTRATANTE");
        q.agregarParametroValues(IND_ACTIVO, ""+estatus+"");
        if(Boolean.FALSE.equals(estatus)) {
        	 q.agregarParametroValues("ID_USUARIO_BAJA", ""+idUsuario+"" );
 			q.agregarParametroValues("FEC_BAJA", ""+AppConstantes.CURRENT_TIMESTAMP+"");
        }else {
        	  q.agregarParametroValues(ID_USUARIO_MODIFICA, ""+idUsuario+"" );
  			q.agregarParametroValues(FEC_ACTUALIZACION, ""+AppConstantes.CURRENT_TIMESTAMP+"");
        }
		q.addWhere("ID_CONTRATANTE =" + idContratante);
        String query = q.obtenerQueryActualizar();
        log.info(query);
        String encoded = encodedQuery(query);
        parametro.put(AppConstantes.QUERY, encoded);
        request.setDatos(parametro);
        return request;
	}


	public DatosRequest  validacionActualizar(String nombre, String paterno, String materno, String rfc, Integer idPersona) {
		DatosRequest request= new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("COUNT(*) AS c")
		.from(SVC_PERSONA)
		.join("SVC_CONTRATANTE SV", "SP.ID_PERSONA = SV.ID_PERSONA");
		queryUtil.where("SP.NOM_PERSONA= :nombre").and("SP.NOM_PRIMER_APELLIDO= :paterno")
		.and("SP.NOM_SEGUNDO_APELLIDO= :materno").and("SP.CVE_RFC= :rfc").and("SP.ID_PERSONA != :id")
		.setParameter("nombre", nombre).setParameter("paterno", paterno).setParameter("materno", materno)
		.setParameter("rfc", rfc).setParameter("id", idPersona);
		String query = obtieneQuery(queryUtil);
		log.info("-> " +query);
		String encoded = encodedQuery(query);
			parametro.put(AppConstantes.QUERY, encoded);
				request.setDatos(parametro);
				return request;
	}


	public Map<String, Object> reporteCatUsrContra(ReporteDto reporte) {
		Map<String, Object> envioDatos = new HashMap<>();
		StringBuilder condition= new StringBuilder();
		if(reporte.getCurp()!=null) {
			condition.append(" AND SP.CVE_CURP= '"+reporte.getCurp()+"'");
		}
	    if(reporte.getNss()!=null) {
			condition.append(" AND SP.CVE_NSS= '"+reporte.getNss()+"'");
		}
	    if (reporte.getNomContratante()!=null) {
			condition.append(" AND SP.NOM_PERSONA LIKE '%"+reporte.getNomContratante()+"%'");
		} 
	    if(reporte.getEstatus()!=null) {
			condition.append(" AND SC.IND_ACTIVO= "+reporte.getEstatus()+"");
		}condition.append(";");
		envioDatos.put("condition", condition.toString());		
		envioDatos.put("rutaNombreReporte", reporte.getRutaNombreReporte());
		envioDatos.put("tipoReporte", reporte.getTipoReporte());
		if(reporte.getTipoReporte().equals("xls")) {
			envioDatos.put("IS_IGNORE_PAGINATION", true);
		}
		return envioDatos;
	}

	public DatosRequest catalogoPais(DatosRequest request) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("ID_PAIS AS id",
				"DES_PAIS AS pais")
		.from("SVC_PAIS");
	    String query = obtieneQuery(queryUtil);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
	    request.getDatos().remove(AppConstantes.DATOS);
	    request.setDatos(parametros);
		return request;
	}

	public DatosRequest catalogoEstado(DatosRequest request) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("ID_ESTADO AS id",
				"DES_ESTADO AS estado")
		.from("SVC_ESTADO");
	    String query = obtieneQuery(queryUtil);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
        request.getDatos().remove(AppConstantes.DATOS);
	    request.setDatos(parametros);
		return request;
	}

	public DatosRequest catalogoCp(DatosRequest request, Integer cp) {
        Map<String, Object> parametro = new HashMap<>();
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("CVE_CODIGO_POSTAL AS codigoPostal", 
        		"DES_COLONIA AS colonia",
                        "DES_MNPIO AS municipio", 
                        "DES_ESTADO AS estado")
                .from("SVC_CP")
                .where("CVE_CODIGO_POSTAL=" + cp);
        String query = obtieneQuery(queryUtil);
        String encoded = encodedQuery(query);
        parametro.put(AppConstantes.QUERY, encoded);
        request.getDatos().remove(AppConstantes.DATOS);
        request.setDatos(parametro);
        return request;
	}
	
	public DatosRequest buscarContra(DatosRequest request, String nombre) {
		 Map<String, Object> parametro = new HashMap<>();
	        SelectQueryUtil queryUtil = new SelectQueryUtil();
	        queryUtil.select("CONCAT(SP.NOM_PERSONA,' ', " 
	        		+"SP.NOM_PRIMER_APELLIDO, ' ', "
	                        +"SP.NOM_SEGUNDO_APELLIDO) AS nombre")
	                .from(SVC_CONTRATANTE)
	                .join(SVC_PERSONA, SC_ID_PERSONA_SP_ID_PERSONA);
	        queryUtil.where("SP.NOM_PERSONA LIKE " +"'%"+nombre +"%'");
	        String query = obtieneQuery(queryUtil);
	        String encoded = encodedQuery(query);
	        parametro.put(AppConstantes.QUERY, encoded);
	        request.getDatos().remove(AppConstantes.DATOS);
	        request.setDatos(parametro);
	        return request;
	}
	
	private static String obtieneQuery(SelectQueryUtil queryUtil) {
        return queryUtil.build();
    }
	
	private static String encodedQuery(String query) {
        return DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
    }

}
