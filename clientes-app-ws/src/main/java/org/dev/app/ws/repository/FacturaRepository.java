package org.dev.app.ws.repository;

import org.dev.app.ws.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("facturaRepository")
public interface FacturaRepository extends JpaRepository<Factura, Long> {

}
