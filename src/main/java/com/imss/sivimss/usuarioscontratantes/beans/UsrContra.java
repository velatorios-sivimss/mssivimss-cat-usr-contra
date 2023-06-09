package com.imss.sivimss.usuarioscontratantes.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.usuarioscontratantes.model.request.FiltrosUsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.util.AppConstantes;
import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsrContra {
	public DatosRequest buscarContratantes(DatosRequest request, FiltrosUsrContraRequest filtros) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("SP.CVE_CURP AS curp",
				"SP.CVE_NSS AS NSS",
				"CONCAT(SP.NOM_PERSONA, ' ',"
				+ "SP.NOM_PRIMER_APELLIDO, ' ',"
				+ "SP.NOM_SEGUNDO_APELLIDO) AS NomContratante",
				"SP.CVE_RFC AS rfc",
				"DATE_FORMAT(SP.FEC_NAC, '%d/%m/%Y') AS fecNacimiento",
				"SP.DES_TELEFONO AS tel",
				"SC.IND_ACTIVO AS estatus")
		.from("SVC_CONTRATANTE SC")
		.join("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA");
	//	if(filtros.GE)
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

}
