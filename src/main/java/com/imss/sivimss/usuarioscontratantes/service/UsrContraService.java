package com.imss.sivimss.usuarioscontratantes.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.Response;

public interface UsrContraService {

	Response<Object> buscarContratantes(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> detalleContratante(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> modificarContratante(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> cambiarEstatusContratante(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> descargarCatContratantes(DatosRequest request, Authentication authentication)throws IOException;

	Response<Object> buscarCatalogos(DatosRequest request, Authentication authentication) throws IOException;

}
