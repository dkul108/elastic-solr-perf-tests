totals=./build/jmperf-totals

rm -v -i -r $totals
mkdir $totals

for filename in ./build/jmperf/*.csv; do
 [ -e "$filename" ] || continue
 name=${filename##*/}
 JMeterPluginsCMD.sh --plugin-type AggregateReport \
  --input-jtl $filename \
  --generate-csv $totals/$name
 cat $totals/$name | while read line; do 
  echo "$name:$line" >> $totals/totals.csv 
 done
done