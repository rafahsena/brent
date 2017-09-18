/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ordenador;

import ENEM.Aluno;
import java.nio.ByteBuffer;

/**
 *
 * @author Jocelino Neto
 */
public interface IBrentOrganizer {

    /**
     * TODO - Função HASH que calcula posição.
     *
     * @param matricula
     * @return
     */
    public long getHash(long matricula);

    /**
     * TODO - Função que calcula quantidade de incrementos em posição.
     *
     * @param matricula
     * @return
     */
    public long getIncremento(long matricula);

    /**
     * TODO - Este método irá verificar a quantidade de pulos de um registro caso aja
     * choque em uma posição.
     *
     * @param aluno
     * @param hash
     * @param incremento
     * @return
     */
    public int contaPulos(Aluno aluno, long hash, long incremento);
}
