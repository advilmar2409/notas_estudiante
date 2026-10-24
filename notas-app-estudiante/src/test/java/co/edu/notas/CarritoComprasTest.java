package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * ENTREGA DEL TALLER
 * Nombre completo:
 * 1. Bug encontrado (método y línea de CarritoCompras.java):
 * 2. Prueba que lo detectó (nombre del método):
 * 3. Sugerencia de la IA que rechacé o corregí, y por qué
 *    (si todas eran correctas: cómo verifiqué una de ellas con las reglas):
 */
class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() { carrito = new CarritoCompras(); }

    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
        // Arrange
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
        // Act
        double subtotal = carrito.calcularSubtotal();
        // Assert
        assertEquals(34_000, subtotal, 0.001);
    }

        @Test
    @DisplayName("Un carrito vacío tiene subtotal 0 y total 0")
    void calcularTotal_carritoVacio_subtotalYTotalSonCero() {
        // Arrange: el carrito nuevo ya viene vacío desde setUp()
        // Act
        double subtotal = carrito.calcularSubtotal();
        double total = carrito.calcularTotal();
        // Assert
        assertEquals(0, subtotal, 0.001);
        assertEquals(0, total, 0.001);
    }

    @Test
    @DisplayName("Un producto con nombre vacío se rechaza")
    void agregarProducto_nombreVacio_lanzaExcepcion() {
        // Arrange: carrito vacío de setUp()
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("", 5_000, 1));
    }

    @Test
    @DisplayName("Un producto con nombre nulo se rechaza")
    void agregarProducto_nombreNulo_lanzaExcepcion() {
        // Arrange: carrito vacío de setUp()
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto(null, 5_000, 1));
    }

    @Test
    @DisplayName("Un producto con precio 0 se rechaza")
    void agregarProducto_precioCero_lanzaExcepcion() {
        // Arrange: carrito vacío de setUp()
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 0, 1));
    }

    @Test
    @DisplayName("Un producto con cantidad 0 se rechaza")
    void agregarProducto_cantidadCero_lanzaExcepcion() {
        // Arrange: carrito vacío de setUp()
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 5_000, 0));
    }
    
        @Test
    @DisplayName("SENA10 descuenta el 10 % del subtotal")
    void calcularDescuento_cuponSena10_descuentaDiezPorCiento() {
        // Arrange: subtotal 100.000 -> 10 % = 10.000
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        // Act
        double descuento = carrito.calcularDescuento();
        // Assert
        assertEquals(10_000, descuento, 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO con subtotal exacto de 50.000 descuenta 5.000")
    void calcularDescuento_bienvenidoSubtotalCincuentaMil_descuentaCincoMil() {
        // Arrange: subtotal = 50.000 (la frontera: "igual o mayor")
        carrito.agregarProducto("Mochila", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        // Act
        double descuento = carrito.calcularDescuento();
        // Assert
        assertEquals(5_000, descuento, 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO con subtotal de 49.999 no descuenta")
    void calcularDescuento_bienvenidoSubtotalBajoElMinimo_noDescuenta() {
        // Arrange: subtotal = 49.999 (un peso por debajo de la frontera)
        carrito.agregarProducto("Mochila", 49_999, 1);
        carrito.aplicarCupon("BIENVENIDO");
        // Act
        double descuento = carrito.calcularDescuento();
        // Assert
        assertEquals(0, descuento, 0.001);
    }

    @Test
    @DisplayName("Un cupón inexistente se rechaza")
    void aplicarCupon_cuponInvalido_lanzaExcepcion() {
        // Arrange: carrito vacío de setUp()
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> carrito.aplicarCupon("PROMO99"));
    }

    @Test
    @DisplayName("Total con envío pagado: 100.000 -> 131.000")
    void calcularTotal_subtotalBajo_cobraEnvio() {
        // Arrange: 100.000 x 1,19 = 119.000 (menor a 150.000) -> + 12.000 de envío
        carrito.agregarProducto("Libro", 100_000, 1);
        // Act
        double total = carrito.calcularTotal();
        // Assert
        assertEquals(131_000, total, 0.001);
    }

    @Test
    @DisplayName("Total con envío gratis y SENA10: 500.000 -> 535.500")
    void calcularTotal_conSena10YEnvioGratis_totalCorrecto() {
        // Arrange: (500.000 - 50.000) x 1,19 = 535.500 (mayor a 150.000) -> envío gratis
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        // Act
        double total = carrito.calcularTotal();
        // Assert
        assertEquals(535_500, total, 0.001);
    }
    // TODO: SENA10, BIENVENIDO (50.000 y 49.999), cupón inválido, totales
}
