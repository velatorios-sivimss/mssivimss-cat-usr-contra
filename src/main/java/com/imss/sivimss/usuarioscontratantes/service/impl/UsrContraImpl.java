package com.imss.sivimss.usuarioscontratantes.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.imss.sivimss.usuarioscontratantes.model.request.CatalogoRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.FiltrosUsrContraRequest;
import com.imss.sivimss.usuarioscontratantes.model.request.ReporteDto;
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
	
	 private static final String DIAGONAL="/";
	 private static final String PATH_PAGINADO="paginado";
	 private static final String PATH_CONSULTA="consulta";
	 private static final String PATH_CREAR_MULTIPLE="crearMultiple";
	 private static final String PATH_ACTUALIZAR="actualizar";
	
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
		Response<?> response = providerRestTemplate.consumirServicio(usrContra.buscarContratantes(request, filtros).getDatos(), urlConsulta+DIAGONAL+PATH_PAGINADO,
					authentication);
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA CONTRATANTES OK", CONSULTA, authentication, usuario);
			return response;
	}

	@Override
	public Response<?> detalleContratante(DatosRequest request, Authentication authentication) throws IOException { 
		List<UsrContraResponse> usrResponse;
		Response<?> response = providerRestTemplate.consumirServicio(usrContra.verDetalle(request).getDatos(), urlConsulta+DIAGONAL + PATH_CONSULTA,
				authentication);
		if (response.getCodigo() == 200) {
			usrResponse = Arrays.asList(modelMapper.map(response.getDatos(), UsrContraResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(usrResponse));
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DETALLE CONTRATANTE OK", CONSULTA, authentication, usuario);
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
			Response<?>	 response = providerRestTemplate.consumirServicio(usrContra.insertarPersona().getDatos(), urlConsulta+DIAGONAL + PATH_CREAR_MULTIPLE,
						authentication);
		if(response.getCodigo()==200){
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Datos generales correctos", ALTA, authentication, usuario);
			providerRestTemplate.consumirServicio(usrContra.insertarDomic().getDatos(), urlConsulta+DIAGONAL + PATH_CREAR_MULTIPLE,
					authentication);
		}
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Domicilio contratante correcto", ALTA, authentication, usuario);
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
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsrContraRequest usrContraR = gson.fromJson(datosJson, UsrContraRequest.class);	
		if(usrContraR.getIdPersona()==null || usrContraR.getIdDomicilio()==null){
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),INFORMACION_INCOMPLETA, MODIFICACION, authentication, usuario);
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		} 
		Response<?> response = providerRestTemplate.consumirServicio(usrContra.validacionActualizar(usrContraR.getNombre(), usrContraR.getPaterno(), usrContraR.getMaterno(), usrContraR.getRfc(), usrContraR.getIdPersona()).getDatos(), urlConsulta +DIAGONAL+ PATH_CONSULTA,
				authentication);
		Object rst=response.getDatos();
		log.info("---> "+rst.toString());
		if(rst.toString().equals("[{c=1}]")) {
			response.setMensaje("56");
			response.setDatos(null);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"REGISTRO DUPLICADO", CONSULTA, authentication, usuario);
			return response;
			}
		
		try {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		usrContra = new UsrContra(usrContraR);
		Date dateF = new SimpleDateFormat("dd-MM-yyyy").parse(usrContraR.getFecNacimiento());
        DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
        String fecha=fechaFormat.format(dateF);
        usrContra.setFecNacimiento(fecha);
		usrContra.setIdUsuario(usuarioDto.getIdUsuario());
		response = providerRestTemplate.consumirServicio(usrContra.editarPersona().getDatos(), urlConsulta+DIAGONAL + PATH_ACTUALIZAR,
					authentication);
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"PERSONA ACTUALIZADA CORRECTAMENTE", MODIFICACION, authentication, usuario);
			if(response.getCodigo()==200) {
				providerRestTemplate.consumirServicio(usrContra.editarDomic().getDatos(), urlConsulta +DIAGONAL+PATH_ACTUALIZAR,
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
	  Response<?> response = providerRestTemplate.consumirServicio(usrContra.cambiarEstatus(usrContraR.getEstatus(), usrContraR.getIdContratante()).getDatos(), urlConsulta+DIAGONAL +PATH_ACTUALIZAR,
				authentication);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Todo correcto", BAJA, authentication, usuario);
	return response;
	}
	
	/*private boolean validarRegistro(String nombre, String paterno, String materno, String rfc, Integer idPersona, Authentication authentication) throws IOException {
		Response<?> response= providerRestTemplate.consumirServicio(usrContra.validacionActualizar(nombre, paterno, materno, rfc, idPersona).getDatos(), urlConsulta + PATH_CONSULTA,
				authentication);
		if (response.getCodigo()==200){
	Object rst=response.getDatos();
	log.info("---> "+rst.toString());
	return rst.toString().equals("[]");
		}
		 throw new BadRequestException(HttpStatus.BAD_REQUEST, "ERROR AL REGISTRAR EL CONTRATANTE");
	}*/

	@Override
	public Response<?> descargarCatContratantes(DatosRequest request, Authentication authentication)
			throws IOException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteDto reporte= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = new UsrContra().reporteCatUsrContra(reporte);
		Response<?> response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,
				authentication);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"SE GENERO CORRECRAMENTE EL REPORTE DE USUARIOS CONTRATANTES", IMPRIMIR, authentication, usuario);
		return response;
	}

	@Override
	public Response<?> buscarCatalogos(DatosRequest request, Authentication authentication) throws IOException {
		Response<?> response = null;
		String datosJson = String.valueOf(request.getDatos().get("datos"));
		CatalogoRequest catalogo = gson.fromJson(datosJson, CatalogoRequest.class);
		if(catalogo.getIdCatalogo()==1) {
		     response = providerRestTemplate.consumirServicio(usrContra.catalogoPais(request).getDatos(), urlConsulta+DIAGONAL + PATH_CONSULTA,
					authentication);	
		}else if(catalogo.getIdCatalogo()==2) {
			 response = providerRestTemplate.consumirServicio(usrContra.catalogoEstado(request).getDatos(), urlConsulta+DIAGONAL + PATH_CONSULTA,
					authentication);
		}else if(catalogo.getIdCatalogo()==3) {
			  response = providerRestTemplate.consumirServicio(usrContra.catalogoCp(request, catalogo.getCp()).getDatos(), urlConsulta+DIAGONAL + PATH_CONSULTA,
					authentication);	
		}else if(catalogo.getIdCatalogo()>3 || catalogo.getIdCatalogo()==null) {
			 logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"INFORMACION INCOMPLETA", CONSULTA, authentication, usuario);
			 throw new BadRequestException(HttpStatus.BAD_REQUEST, "FALTA_INFORMACION");
		}
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CATALOGOS OK", CONSULTA, authentication, usuario);
			return response;		
	}
	}

