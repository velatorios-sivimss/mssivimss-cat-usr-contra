package com.imss.sivimss.usuarioscontratantes.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.Response;

public interface UsrContraService {

	Response<?> buscarContratantes(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> detalleContratante(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> modificarContratante(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> cambiarEstatusContratante(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> descargarCatContratantes(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> buscarCatalogos(DatosRequest request, Authentication authentication) throws IOException;

}
