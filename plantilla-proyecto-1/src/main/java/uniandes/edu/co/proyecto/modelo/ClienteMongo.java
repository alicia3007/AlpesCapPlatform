package uniandes.edu.co.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clientes")
public class ClienteMongo {

    @Id
    private String id;

    private String tipo;
    private String nombre;
    private String correo;
    private String numero;
    private String cedula;

    public ClienteMongo() {}

    public ClienteMongo(String tipo, String nombre, String correo, String numero, String cedula) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.correo = correo;
        this.numero = numero;
        this.cedula = cedula;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
}
