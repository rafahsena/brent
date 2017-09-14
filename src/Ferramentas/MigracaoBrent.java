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
        File fOrigem = new File("aluno.db");
        RandomAccessFile fileOrigem = new RandomAccessFile(fOrigem, "r");
        FileChannel channelOrigem = fileOrigem.getChannel();
        
        File fDestino = new File("enem_brent.db"); // referencia o arquivo organizado pelo método implementado
        IFileOrganizer org = new OrganizadorBrent("enem_brent.db");

        // Ler cada aluno do arquivo de origem e inserir no de destino
        for (int i=0; i<5; i++)  {
            // Ler da origem
            ByteBuffer buff = ByteBuffer.allocate(200);
            channelOrigem.read(buff);
            

            buff.flip();
            Aluno a = Conversor.toAluno(buff);
            
            org.addAluno(a);
            // Inserir no destino
            //org.addReg(a);
        }
        
        //org.getAll();
        
        channelOrigem.close();
    }

}