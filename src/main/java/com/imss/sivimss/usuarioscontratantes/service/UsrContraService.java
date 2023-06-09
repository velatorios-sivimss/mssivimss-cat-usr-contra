package com.imss.sivimss.usuarioscontratantes.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.usuarioscontratantes.util.DatosRequest;
import com.imss.sivimss.usuarioscontratantes.util.Response;

public interface UsrContraService {

	Response<?> buscarContratantes(DatosRequest request, Authentication authentication) throws IOException;

}
