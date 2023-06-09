package com.imss.sivimss.usuarioscontratantes.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.usuarioscontratantes.beans.UsrContra;
import com.imss.sivimss.usuarioscontratantes.model.request.FiltrosUsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.UsuarioDto;
import com.imss.sivimss.usuarioscontratantes.model.response.UsrContraResponse;
import com.imss.sivimss.usuarioscontratantes.service.UsrContraService;
import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.LogUtil;
import com.imss.sivimss.usuarioscontratantes.util.ProviderServiceRestTemplate;
import com.imss.sivimss.usuarioscontratantes.util.Response;

@Service
public class UsrContraImpl implements UsrContraService {

	
	private static final String ALTA = "alta";
	private static final String BAJA = "baja";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	private static final String IMPRIMIR = "imprimir";
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	
	 private static final String PATH_PAGINADO="/paginado";
	 private static final String PATH_INSERTAR_MULTIPLE="/insertarMultiple";
	
	@Autowired
	private LogUtil logUtil;

	@Value("${endpoints.mod-catalogos}")
	private String urlConsulta;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	Gson gson = new Gson();
	
	@Autowired
	private ModelMapper modelMapper;
	
	UsrContra usrContra = new UsrContra();
	
	UsuarioDto usuario;
	
	@Override
	public Response<?> buscarContratantes(DatosRequest request, Authentication authentication) throws IOException {
		String datosJson = String.valueOf(request.getDatos().get("datos"));
		FiltrosUsrContraRequest filtros = gson.fromJson(datosJson, FiltrosUsrContraRequest.class);
		 Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
	        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
	        filtros.setTamanio(tamanio.toString());
	        filtros.setPagina(pagina.toString());
	        List<UsrContraResponse> usrResponse;
		Response<?> response = providerRestTemplate.consumirServicio(usrContra.buscarContratantes(request, filtros).getDatos(), urlConsulta+PATH_PAGINADO,
					authentication);
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA CONTRATANTES OK", CONSULTA, authentication, usuario);
		/*	if (response.getCodigo() == 200) {
				usrResponse = Arrays.asList(modelMapper.map(response.getDatos(), UsrContraResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(usrResponse));
			} */
			return response;
	}
}
