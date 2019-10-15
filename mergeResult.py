import os
import csv
import fileinput
from operator import add


artifact_directory = "./artifacts"
form_directories = ("RZ/", "NRZ/", "NRZT/")
output_file = f"{artifact_directory}/mergedResult.csv"

fnames = []
fvalues = []
for dir in form_directories:
    # GET FILES LIST
    print(f"Merging files from {artifact_directory}/{dir} ...")
    path = f"{artifact_directory}/{dir}"
    files = os.listdir(path)

    # INIT TEB LIST
    file = files[0]
    R = csv.DictReader(open(f"{path}/{file}"), delimiter=',')
    fnames=R.fieldnames
    teb_array = [0.0] * len(R.fieldnames)

    # SUM LIST TO GET MEAN VALUES OF EACH SNR
    for file in files:
            R = csv.DictReader(open(f"{path}/{file}"), delimiter=',')
            row = next(R)
            teb_array_in = [float(i.replace(',', '.')) for i in row.values()]
            teb_array = list(map(add, teb_array, teb_array_in) )

    # STORE RESULT OF EACH DIR
    values = [r/len(files) for r in teb_array]
    fvalues.append(dict(zip(fnames, values)))

# PREPARE OUTPUT CSV
f = open(output_file, 'w')
with f:
    form_label = 'FORM'
    fnames.insert(0,form_label)

    # OUTPUT CSV FILES : FORM,SNR...
    writer = csv.DictWriter(f, fieldnames=fnames, delimiter=';')
    writer.writeheader()
    for i,row in enumerate(fvalues):
        row[form_label] = form_directories[i][:-1]
        writer.writerow(row)

with fileinput.FileInput(output_file, inplace=True) as file:
    for line in file:
        print(line.replace('.', ','), end='')