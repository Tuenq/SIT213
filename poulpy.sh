counter=0
counter_success=0
declare -A running_task
declare -A result_teb
cmd_to_execute="cd ~/Projets/SIT213/ && ant generateCurves"

### Starting by cleaning the project
ant cleanAll compileApp

### Starting the process on remote computer
for ssh_host in $(cat ssh_hosts.txt)
do
  echo "Connexion à [$ssh_host] ..."
  ssh s18hugde@"$ssh_host" -o ConnectTimeout=1 -o StrictHostKeyChecking=no "$cmd_to_execute" >/dev/null 2>&1 &
  running_task[$ssh_host]=$!
done

### Checking status of all remote computer background process
counter=${#running_task[@]}
for key in "${!running_task[@]}";
do
#  echo "$key ---- ${running_task[$key]}";
  if wait ${running_task[$key]}; then
    echo COMPLETED
    ((counter_success=counter_success+1))
  else
    echo NOT COMPLETED
  fi
done

### Merge result
python3 mergeResult.py

echo ""
echo "------------------------------------------------------------------------------"
echo "==> Opération réussi sur $counter_success machines ($counter machines testées)"
echo "------------------------------------------------------------------------------"
