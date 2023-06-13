package com.imss.sivimss.usuarioscontratantes.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.usuarioscontratantes.beans.UsrContra;
import com.imss.sivimss.usuarioscontratantes.exception.BadRequestException;
import com.imss.sivimss.usuarioscontratantes.model.request.FiltrosUsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.UsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.UsuarioDto;
import com.imss.sivimss.usuarioscontratantes.model.response.UsrContraResponse;
import com.imss.sivimss.usuarioscontratantes.service.UsrContraService;
import com.imss.sivimss.usuarioscontratantes.util.AppConstantes;
import com.imss.sivimss.usuarioscontratantes.util.ConvertirGenerico;
import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.LogUtil;
import com.imss.sivimss.usuarioscontratantes.util.ProviderServiceRestTemplate;
import com.imss.sivimss.usuarioscontratantes.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsrContraImpl implements UsrContraService {

	
	private static final String ALTA = "alta";
	private static final String BAJA = "baja";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	private static final String IMPRIMIR = "imprimir";
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	
	 private static final String PATH_PAGINADO="/paginado";
	 private static final String PATH_CONSULTA="/consulta";
	 private static final String PATH_CREAR_MULTIPLE="/crearMultiple";
	 private static final String PATH_ACTUALIZAR="/actualizar";
	
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
		Response<?> response = providerRestTemplate.consumirServicio(usrContra.buscarContratantes(request, filtros).getDatos(), urlConsulta+PATH_PAGINADO,
					authentication);
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA CONTRATANTES OK", CONSULTA, authentication, usuario);
			return response;
	}

	@Override
	public Response<?> detalleContratante(DatosRequest request, Authentication authentication) throws IOException { 
		List<UsrContraResponse> usrResponse;
		Response<?> response = providerRestTemplate.consumirServicio(usrContra.verDetalle(request).getDatos(), urlConsulta + PATH_CONSULTA,
				authentication);
		if (response.getCodigo() == 200) {
			usrResponse = Arrays.asList(modelMapper.map(response.getDatos(), UsrContraResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(usrResponse));
		}
		return response;
	}

	@Override
	public Response<?> altaContratante(DatosRequest request, Authentication authentication) throws IOException {
		try {
			String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		    UsrContraRequest contraRequest = gson.fromJson(datosJson, UsrContraRequest.class);	
			UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		    usrContra = new UsrContra(contraRequest);
			usrContra.setIdUsuario(usuarioDto.getIdUsuario());
			Response<?>	 response = providerRestTemplate.consumirServicio(usrContra.insertarPersona().getDatos(), urlConsulta + PATH_CREAR_MULTIPLE,
						authentication);
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Estatus OK", ALTA, authentication, usuario);
			return response;		
			
		}catch (Exception e) {
			String consulta = usrContra.insertarPersona().getDatos().get(""+AppConstantes.QUERY+"").toString();
			String encoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar la query " +encoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Error al ejecutar la query", CONSULTA, authentication, usuario);
			throw new IOException("5", e.getCause());
		} 
	}

	@Override
	public Response<?> modificarContratante(DatosRequest request, Authentication authentication) throws IOException {
		Response<?> response;
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsrContraRequest usrContraR = gson.fromJson(datosJson, UsrContraRequest.class);	
		if(usrContraR.getIdPersona()==null || usrContraR.getIdDomicilio()==null){
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Informacion Incompleta", MODIFICACION, authentication, usuario);
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta ");
		}
		try {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		usrContra = new UsrContra(usrContraR);
		usrContra.setIdUsuario(usuarioDto.getIdUsuario());
		response = providerRestTemplate.consumirServicio(usrContra.editarPersona().getDatos(), urlConsulta + PATH_ACTUALIZAR,
					authentication);
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"PERSONA ACTUALIZADA CORRECTAMENTE", MODIFICACION, authentication, usuario);
			if(response.getCodigo()==200) {
				providerRestTemplate.consumirServicio(usrContra.editarDomic().getDatos(), urlConsulta + PATH_ACTUALIZAR,
						authentication);
			}
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DOMICILIO MODIFICADO CORRECTAMENTE", MODIFICACION, authentication, usuario);
			return response;		
	}catch (Exception e) {
		String consulta = usrContra.editarPersona().getDatos().get("query").toString();
		String encoded = new String(DatatypeConverter.parseBase64Binary(consulta));
		log.error("Error al ejecutar la query" +encoded);
		logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"error", MODIFICACION, authentication, usuario);
		throw new IOException("5", e.getCause()) ;
	}
	}

	@Override
	public Response<?> cambiarEstatusContratante(DatosRequest request, Authentication authentication) throws IOException {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		usrContra.setIdUsuario(usuarioDto.getIdUsuario());
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsrContraRequest usrContraR = gson.fromJson(datosJson, UsrContraRequest.class);	
	  Response<?> response = providerRestTemplate.consumirServicio(usrContra.cambiarEstatus(usrContraR.getEstatus(), usrContraR.getIdContratante()).getDatos(), urlConsulta +PATH_ACTUALIZAR,
				authentication);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Todo correcto", BAJA, authentication, usuario);
	return response;
	}

	}
