counter=0
counter_success=0
cmd_to_execute="echo OK"

ant cleanAll

for ssh_host in $(cat ssh_hosts.txt)
do
  echo "Connexion à [$ssh_host] ..."
  ssh s18hugde@"$ssh_host" -o ConnectTimeout=1 -o StrictHostKeyChecking=no $cmd_to_execute
  if [ $? -eq 0 ]; then
    ((counter_success=counter_success+1))
  fi
  ((counter=counter+1))
done

echo "==> Opération réussi sur $counter_success machines (/$counter machines testées)"

# CHEMIN PROJET : ~/Projets/SIT213/