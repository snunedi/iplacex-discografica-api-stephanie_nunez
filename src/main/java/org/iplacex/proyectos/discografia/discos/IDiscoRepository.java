package org.iplacex.proyectos.discografia.discos;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface IDiscoRepository extends MongoRepository<Disco, String> {


    List<Disco> findByIdArtista(String idArtista);
}