package rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import modelo.UsuarioDTO;

@XmlRootElement
public class Listado {
	public static class ResumenExtendido {
		private String url;
		private UsuarioDTO dto;
		// Métodos get y set
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public UsuarioDTO getDTO() {
			return dto;
		}
		public void setDTO(UsuarioDTO dto) {
			this.dto = dto;
		}
		
		
	}

	private List<ResumenExtendido> usuario;
	// Métodos get y set

	public List<ResumenExtendido> getUsuario() {
		return usuario;
	}

	public void setUsuario(List<ResumenExtendido> usuario) {
		this.usuario = usuario;
	}
	
	
}