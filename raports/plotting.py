from plotnine import ggplot, aes, geom_point, scale_color_gradient, labs, theme_minimal, geom_path
import pandas as pd
import matplotlib.pyplot as plt


def load_coordinates_from_csv(csv_path):
    return pd.read_csv(csv_path, sep=';', header=None)


def prepare_data_for_plot(best_solution, coordinates_df):
    rows = []
    city_order = best_solution['cityOrder']

    for node_index in city_order:
        coord = coordinates_df.iloc[node_index]
        rows.append({
            'x': coord[0],
            'y': coord[1],
            'cost': coord[2],
        })
    rows.append(rows[0])
    return pd.DataFrame(rows)


def plot_solution(solution, coordinates_df, title):
    solution_df = prepare_data_for_plot(solution, coordinates_df)
    coordinates_df.columns = ["x","y","cost"]

    p = (ggplot() +
         geom_path(aes(x='x', y='y'), data=solution_df, color='black', size=1) +
         geom_point(aes(x='x', y='y', color='cost', size='cost'), data=coordinates_df, alpha=0.5) +
         scale_color_gradient(low='blue', high='red') +
         labs(title=title) +
         theme_minimal())

    return p


def plot_all(solver_types, instances, coordinates, best_solutions):
    for solver in solver_types:
        for instance in instances:
            best_solution = best_solutions[f'{solver}_{instance}']
            title = f"Best Solution from Instance {instance} ({solver.capitalize()})"
            plot_solution(best_solution, coordinates[instance], title).show()


def plot_similarity_data(data, title):
    x, y = data[:, 0], data[:, 1]
    plt.scatter(x, y)
    plt.xlabel("Objective function")
    plt.ylabel("Similarity value")
    plt.title(title)
    plt.show()
