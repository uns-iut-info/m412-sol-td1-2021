#!/bin/bash
#
# 2016 02 18
#

# javac *.java dans sum/

#OAR -n somme
#OAR -p cluster='dellr900' AND mem>=64000
#OAR -l nodes=1/core=24,walltime=03:30:00
#OAR -O somme%jobid%

# $ oarsub -S ./somme.sh
# $ oarstat -j 128756
# $ oarpeek -f 128758
# $ oardel 128758
# $ oarstat -j 128891 -f


export JAVA_HOME=$HOME/jdk1.8.0_51
echo $JAVA_HOME

size=1048576 # 1024 * 1024
#size=2147483648
MAX_SIZE=${1:-2147483648} # 2^31

while (( size <= $MAX_SIZE ))
do
    echo $size
    (( length = $size - 6 )) # array of 2147483648 elements overflows
    echo $JAVA_HOME/bin/java -Xmx32G -Xms32G sum.SommeTab $length ${2:-100}
    $JAVA_HOME/bin/java -Xmx32G -Xms32G sum.SommeTab $length ${2:-100}
    (( size = $size * 2 ))
done


