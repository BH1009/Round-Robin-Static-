/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roundrobinstatic;

import java.util.Queue;
import java.util.LinkedList;

/**
 *
 * @author herna
 */
public class RoundRobinStatic {
 //Cola de procesos
    static Queue<String> cola = new LinkedList<>(); 
  
    //Arreglos de informacion de procesos los procesos
    static String proceso[] = {"A", "B", "C", "D", "E"};
    static byte uso[] = {3, 5, 2, 6, 4};
    static byte rafaga[] = {3, 5, 2, 6, 4};
    static byte llegada[] = {0, 1, 4, 5, 8};
    
    //Numero de procesos
    static int np = uso.length;
    
    //Variables adicionales
    static byte finalizacion[] = new byte[np];
    static int retorno[] = new int[np];
    static int espera[] = new int[np];
    static int servicio[] = new int[np];
    static float indice[] = new float[np];
    
    //Quantum
    static byte q = 3;
    
    //Tiempo total
    static int total = 0;
    
    //Variables temporales
    String aux;
    
    String lista[] =  new String[20];

    
    

    public static void main(String[] args) {
        //Objeto
        RoundRobinStatic obj = new RoundRobinStatic();
        
        System.out.println("*Planificador Round Robin*");
        System.out.println(" ");
        System.out.println("Quantum: " + q);
        System.out.println(" ");
        obj.tiempoTotal();
        obj.MostrarProcesos();
        obj.RoundRobin();
        obj.MostrarProcesosFinalizados();
        obj.Diagrama();
    }
    
    
    //Metodo que calcula el tiempo total que estara en uso el cpu
    public void tiempoTotal(){
        for(byte i =0; i < np; i++){
            total += uso[i];//Esta variable almacena el tiempo de uso de todos los procesos
        }
    }//Fin del metodo de tiempototal

    //Metodo que muestra todos los procesos agregados ademas de la cola de procesos 
    public void MostrarProcesos() {
        System.out.println("Lista de Procesos Ingresados");
        System.out.println();
        System.out.println("Proceso|Llegada|Uso|Rafaga|");
        for (byte i = 0; i < np; i++) {
            System.out.println(proceso[i]+"      |"+llegada[i]+"      |"+rafaga[i]+"  |"+uso[i]+"     |");
        }
        System.out.println("Cola: " + cola);
        System.out.println(" ");
    }//Fin de metodo
    
    //Calculos de los procesos
    public void Calculos(){
        for (byte i = 0; i < np; i++) {
            retorno[i] = finalizacion[i] - llegada[i] ;
        }
        
        for (byte i = 0; i < np; i++) {
            espera[i] = finalizacion[i] - llegada[i] - rafaga[i];
        }
        
        for (byte i = 0; i < np; i++) {
            servicio[i] = retorno[i] - espera[i];
        }
        
        for (byte i = 0; i < np; i++) {
            indice[i] = (float)servicio[i] / retorno[i];
        }
        
    }//Fin de metodo
    
    //Muestra los procesos finalizados con sus respectivos calculos
    public void MostrarProcesosFinalizados() {
        Calculos();
        System.out.print("");
        System.out.println("Lista de Procesos Finalizados");
        System.out.println();
        System.out.println("Proceso|Llegadda|Uso|Finalizacion|Retorno|Espera|Servicio|Indice|");
        for (byte i = 0; i < np; i++) {
            System.out.println(proceso[i] + "       " + llegada[i] + "        " + rafaga[i]+"   "+finalizacion[i]+"            "+retorno[i]+"       "+espera[i]+"      "+servicio[i]+"        "+String.format("%.2f",indice[i]));
        }
        System.out.println("");
    }//Fin de metodo
    
    //Metodo que agreage un procesos por su tiempo de llegada 
    public void agregarLlegada(int x){
        for(int i = 0; i < np; i++){
            if(llegada[i] == x){
                String p = proceso[i];
                agregarProceso(p);
            }
        }
    }
    
    //Agrega un proceso a la cola
    public void agregarProceso(String p){
        cola.add(p);
    }//Fin de metodo
    
    public void RoundRobin(){//Inicio del metodo round robin
        
        //Posicionador 
        byte y = 0;//almacena la posicion del procesos que esta en ejecucion para realizar el calculo
        //Tiempo 
        byte t = 0;
        //Contador
        byte cont = 0;
        
        agregarLlegada(t);//Se pasa el tiempo que transcurre si hay un proceso que llegue en ese instante se agrega a la cola
        while (t < total){//Se realiza el calculo hasta que el tiempo que transcurra sea igual al tiempo total de uso de cpu
            MostrarProcesos();//Se muestran los procesos ademas de la cola 
            String cabeza = cola.peek();//Se recupera el valor de la cabeza de la cola 
            for (byte i = 0; i < np; i++) {//Se recorre el arreglo
                if (cabeza.equals(proceso[i])) {//Se compara el valor de cabeza con el del procesos el posicion i
                    y = i;//Este valor alamcena la posicon del proceso que pasara a cpu
                    /*
                    Los procesos que entran solo pueden hacer dos cosas hacer uso del quantum y dejar la cpu al terminar
                    o hacer uso del quantum y esperar al final de la cola
                    */
                    if (uso[y] <= q) {//Si el uso de cpu es menor o igual al quantum
                        cont += uso[y];//alamacena el tiempo que transcurre durante la ejecucion del proceso
                        for (byte j = 0; j < uso[y]; j++) {//Simula el tiempo que dura el quantum
                            lista[t] = proceso[y];
                            t++;//aumenta el tiempo transcurrido 
                            agregarLlegada(t);//Se pasa el valor del tiempo para saber si hay que agrrgar otro proceso
                        }
                        uso[y] -= uso[y];//Se le resta tiempo de uso de cpu a uso 
                        finalizacion[y] = cont;//Se agrega el tiempo que tardo en terminar el proceso
                        cola.remove(cola.peek());//Sale el proceso de la cola
                    } else {//Si el uso de cpu es mayor al quantum
                        cont += q;//alamacena el tiempo que transcurre durante la ejecucion del proceso
                        for (byte j = 0; j < q; j++) {//Simula el tiempo que dura el quantum
                            lista[t] = proceso[y];
                            t++;//aumenta el tiempo transcurrido 
                            agregarLlegada(t);//Se pasa el valor del tiempo para saber si hay que agrrgar otro proceso
                        }
                        aux = cola.peek();//Se almacena el procesos para agregarse a la cola
                        uso[y] -= q;//Se le resta tiempo de uso de cpu a uso 
                        cola.remove(cola.peek());//Se quita el proceso del inicio 
                        cola.add(aux);//Se agrega al final
                        
                    }//fin del else
                }//Fin de comparador 
            }//Fin de for que busca la posicion del proceso

        }//fin de Iterador 

    }//Fin de metodo de planificacion
    
    //Muestra un diagrama con el uso de cada proceso
    public void Diagrama(){
        System.out.println("Diagrama: ");
        for(int i = 0; i < lista.length; i++){
            
            System.out.print(lista[i] + "|");
        }
        System.out.println(" ");
        System.out.println(" ");
        
    }//Fin de metodo
    
}
