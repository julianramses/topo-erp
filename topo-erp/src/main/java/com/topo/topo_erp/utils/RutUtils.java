package com.topo.topo_erp.utils;

import org.springframework.stereotype.Component;


@Component
public class RutUtils {

    /** Validar RUT con dígito verificador
     * @param rut completo con guión y dígito verificador (ej: 12345678-9)
     * @return true si el rut es valido
     */
    public boolean validarRut(String rut){
        if (rut == null || rut.isEmpty()){
            return false;
        }

        rut = rut.toUpperCase().replace(".", "").replace("-", "");

        if (rut.length() < 2) {
            return false;
        }

        String rutSinDV = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        return calcularDigitoVerificador(rutSinDV) == dv;
    }

    /** Calcula el dígito verificador del rut
     *
     * @param rut sin digito vertificador (ej: 12345678-9)
     * @return digito verificador (0 a 9 o K)
     */


    public char calcularDigitoVerificador(String rut) {
        int suma = 0;
        int multiplicador = 2;

        // Recorrer el RUT de derecha a izquierda
        for (int i = rut.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(rut.charAt(i));
            suma += digito * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = suma % 11;
        int dvCalculado = 11 - resto;

        if (dvCalculado == 11) {
            return '0';
        } else if (dvCalculado == 10) {
            return 'K';
        } else {
            return Character.forDigit(dvCalculado, 10);
        }


       /** Formatea el rut con puntos y guión
        *@param rut ej: (12.345.678-9)
        */
    }
    public String formatearRut(String rut) {
        if (rut == null || rut.length() < 2) {
            return rut;
        }

        rut = rut.toUpperCase().replace(".", "").replace("-", "");
        String rutSinDV = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        // Formatear con puntos
        StringBuilder formateado = new StringBuilder();
        int contador = 0;

        for (int i = rutSinDV.length() - 1; i >= 0; i--) {
            formateado.insert(0, rutSinDV.charAt(i));
            contador++;
            if (contador == 3 && i > 0) {
                formateado.insert(0, '.');
                contador = 0;
            }
        }

        return formateado.toString() + "-" + dv;
    }

    /**
     * Extrae solo los numeros del rut sin digito verificador
     * @param rut ej: (12.345.678-9)
     * @return (123456789)
     */
    public String extraerNumerosRut(String rut) {
        if (rut == null) {
            return "";
        }

        // Remover puntos, guión y dígito verificador
        rut = rut.replace(".", "").replace("-", "");

        if (rut.length() > 1) {
            // Devolver todo excepto el último carácter (dígito verificador)
            return rut.substring(0, rut.length() - 1);
        }

        return rut;
    }





}
