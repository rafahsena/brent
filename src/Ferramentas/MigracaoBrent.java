package Ferramentas;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import Ordenador.IFileOrganizer;
import ENEM.Aluno;
import Organizador.OrganizadorBrent;

public class MigracaoBrent{

    public static void main(String[] args) throws IOException {
        File fOrigem = new File("enem_aleat.db");
        RandomAccessFile fileOrigem = new RandomAccessFile(fOrigem, "r");
        FileChannel channelOrigem = fileOrigem.getChannel();

        File fDestino = new File("enem_brent.db"); // referencia o arquivo organizado pelo método implementado
        IFileOrganizer org = new OrganizadorBrent(fDestino.getAbsolutePath());

        // Ler cada aluno do arquivo de origem e inserir no de destino
        for (int i=0; i<9276328; i++)  {
            // Ler da origem
            ByteBuffer buff = ByteBuffer.allocate(200);
            channelOrigem.read(buff);

            buff.flip();
            Aluno a = Conversor.toAluno(buff);

            // Inserir no destino
            //org.addReg(a);
        }
        channelOrigem.close();
    }

}