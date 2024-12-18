import json
import os
import pandas as pd
from IPython.core.display import display, HTML
from io import StringIO  

def load_json_files_from_folder(folder_path):
    json_data = []

    for filename in os.listdir(folder_path):
        if filename.endswith('.json'):
            file_path = os.path.join(folder_path, filename)
            with open(file_path, 'r') as file:
                try:
                    data = json.load(file)
                    json_data.append(data)
                except json.JSONDecodeError as e:
                    print(f"Error decoding JSON from {filename}: {e}")

    return json_data


def process_solver_data(solver_type, all_json_data, instance,table):
    data = find_solver_type_and_instance(solver_type, all_json_data, instance)

    table[f"{instance} {solver_type}"] = [find_min(data).get('objective function'), find_max(data).get('objective function'), average_cost(data)]
    return find_min(data)


def find_solver_type_and_instance(type, data, instance):
    return [entry for entry in data if entry.get('solverType') == type if entry.get('instance') == instance]


def find_min(data,column_name='objective function'):
    return min((entry for entry in data if column_name in entry), key=lambda x: x[column_name])


def find_max(data,column_name='objective function'):
    return max((entry for entry in data if column_name in entry), key=lambda x: x[column_name])


def average_cost(data,column_name='objective function'):
    objective_values = [entry[column_name] for entry in data if column_name in entry]

    return sum(objective_values) / len(objective_values) if objective_values else 0


def get_best_solutions_and_table(solver_types,instances,all_json_data):
    best_solutions = {}
    table = pd.DataFrame()
    for solver in solver_types:
        for instance in instances:
            best_solutions[f'{solver}_{instance}'] = process_solver_data(solver, all_json_data, instance,table)

    table.index = ["Minimum", "Maximum", "Average"]

    table = table.astype(int)

    return table, best_solutions


def process_solver_data_2(solver_type, all_json_data, instance,column_name='objective function'):
    data = find_solver_type_and_instance(solver_type, all_json_data, instance)

    avg = round(average_cost(data, column_name),1)
    max_ = round(find_max(data, column_name).get(column_name),1)
    min_ = round(find_min(data, column_name).get(column_name),1)
    return find_min(data), avg, max_, min_


def get_best_solutions_and_vertical_table(solver_types,instances,all_json_data,column_name='objective function'):
    best_solutions = {}
    table = pd.DataFrame(columns=["Method", "Instance A", "Instance B"])
    for solver in solver_types:
        row_data = {"Method": solver}
        for instance in instances:
            best_solutions[f'{solver}_{instance}'], avg, max_, min_ = process_solver_data_2(solver, all_json_data, instance,column_name)
            row_data[f"Instance {instance}"] = f"{avg} ({min_}-{max_})"
        table = pd.concat([table, pd.DataFrame([row_data])], ignore_index=True)

    return table, best_solutions


def print_solutions(solver_types,instances,best_solutions):
    for solver in solver_types:
        print(f"Solver type: {solver}",end="")
        for instance in instances:
            best_solution = best_solutions[f'{solver}_{instance}']
            print(f"""
                \tInstance: {instance}
                \tCity costs: {best_solution.get('cost')}
                \tEdge Length: {best_solution.get('edge length')}
                \tObjective function: {best_solution.get('objective function')}
                \tSolution:\n{best_solution.get('cityOrder')}
                \tSolution length: {len(best_solution.get('cityOrder'))}
                \tNo repeats?: {len(best_solution.get('cityOrder')) == len(set(best_solution.get('cityOrder')))}
                \tStarting from: {best_solution.get("starting city")}
                \tElapsed Time: {best_solution.get("elapsed time")}
""", end="")
            if "iterations" in best_solution: print(f"""
                \tIterations: {best_solution.get("iterations")}
                  """)
        print()

def display_html(table,index=True):
    html_table = table.to_html(index=index)

    display(HTML(html_table))


def read_html(string):
    df = pd.read_html(StringIO(string))
    return df[0]


def load_all_json_data(solver_types, instances='AB', folder_path='../out'):
    data = []
    for solver in solver_types:
        with open(f"{folder_path}/{solver}.json", "r") as file:
            json_file = json.load(file)
            for instance in instances:
                data += json_file.get("solutions").get(instance).get("solutions")

    return data
