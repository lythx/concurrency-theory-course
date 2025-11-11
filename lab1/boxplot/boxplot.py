import matplotlib.pyplot as plt
import os

data_dir = "../../data/"

# Define the file mapping and the labels for the solutions
SOLUTION_FILES = {
    "Naiwne": os.path.join(data_dir, "philosopher-simulation.naive.csv"),
    "Z możliwością zagłodzenia": os.path.join(
        data_dir, "philosopher-simulation.double-check.csv"
    ),
    "Asymetryczne": os.path.join(data_dir, "philosopher-simulation.asymmetric.csv"),
    "Z Arbitrem": os.path.join(data_dir, "philosopher-simulation.arbiter.csv"),
}
SOLUTION_LABELS = list(SOLUTION_FILES.keys())
SOLUTION_COLORS = [
    "#FFC107",
    "#2196F3",
    "#4CAF50",
    "#9C27B0",
]  # Yellow, Blue, Green, Purple


def parse_line_to_floats(line, filename, line_index):
    """Parses a single line of comma-separated string data into a list of floats."""
    try:
        # Clean the line by replacing newlines/carriage returns with commas, then strip
        cleaned_line = line.replace("\n", ",").replace("\r", ",").strip()

        # Split by comma and convert to floats, filtering out empty strings
        data = [float(item.strip()) for item in cleaned_line.split(",") if item.strip()]
        return data
    except Exception as e:
        print(
            f"Warning: Could not parse data on line {line_index + 1} of {filename}. Error: {e}"
        )
        return None  # Return None if parsing fails


def load_and_group_data(files_map):
    """
    Loads data from all solution files, grouping results by line index (representing N).

    Returns:
        tuple: (all_data_for_n, n_labels, num_n_groups)
               - all_data_for_n: A list of data lists, ordered for plotting:
                 [Naive_N1, Double_N1, Asym_N1, Arbiter_N1, Naive_N2, ...]
               - n_labels: The labels for the groups of N (e.g., 'N=5', 'N=6', ...)
               - num_n_groups: The total number of experiments (lines) found.
    """
    # Structure: {Solution_Label: [ [data_for_N1], [data_for_N2], ... ]}
    grouped_solutions_data = {label: [] for label in SOLUTION_LABELS}
    num_n_groups = 0

    for label, filename in files_map.items():
        if not os.path.exists(filename):
            print(f"Error: File not found: {filename}. Skipping.")
            return [], [], 0

        with open(filename, "r") as f:
            lines = f.readlines()

        if label == SOLUTION_LABELS[0]:  # Check only the first file for group count
            num_n_groups = len(lines)
            if num_n_groups == 0:
                print("Error: Files are empty.")
                return [], [], 0

        # Ensure all files have the same number of lines
        if len(lines) != num_n_groups:
            print(
                f"Error: File {filename} has {len(lines)} lines, expected {num_n_groups}. Aborting."
            )
            return [], [], 0

        # Parse data line by line
        for line_index, line in enumerate(lines):
            data = parse_line_to_floats(line, filename, line_index)
            if data is not None:
                grouped_solutions_data[label].append(data)
            else:
                print(f"Aborting due to critical parsing error in {filename}.")
                return [], [], 0

    # Prepare data for matplotlib's boxplot (a single flattened list)
    all_data_for_n = []

    # Iterate N by N: [Naive_N1, Double_N1, Asym_N1, Arbiter_N1], [Naive_N2, ...]
    for i in range(num_n_groups):
        for label in SOLUTION_LABELS:
            all_data_for_n.append(grouped_solutions_data[label][i])

    # Create N labels (e.g., N=5, N=6, N=7...)
    # Assuming N starts at 5 based on the problem context.
    n_labels = [
        len(grouped_solutions_data[SOLUTION_LABELS[0]][i]) for i in range(num_n_groups)
    ]

    return all_data_for_n, n_labels, num_n_groups


def generate_grouped_box_plot(
    all_data_for_n,
    n_labels,
    num_n_groups,
    output_filename="philosopher_wait_time_grouped_boxplot.png",
):
    """
    Generates a grouped box plot where 4 boxes (solutions) are grouped for each N.
    """
    num_solutions = len(SOLUTION_LABELS)
    fig, ax = plt.subplots(figsize=(7, 7))

    # Calculate positions for the box plots
    # We have 'num_solutions' boxes per group. We want separation between groups.
    # Group width is num_solutions * box_width + gap.
    group_width = num_solutions + 1.5  # 1.5 is the gap between groups

    # Generate positions for the 4*N boxes
    positions = []
    for i in range(num_n_groups):
        base = i * group_width
        # Positions are [base, base+1, base+2, base+3] for a group of 4
        positions.extend([base + j * 1.0 for j in range(num_solutions)])

    # Generate the box plots
    boxplot_data = ax.boxplot(
        all_data_for_n,
        positions=positions,
        widths=0.8,
        patch_artist=True,
        medianprops={"color": "black"},
        flierprops={
            "marker": "o",
            "markerfacecolor": "red",
            "markersize": 4,
            "alpha": 0.6,
        },
    )

    # Color the boxes (repeating the color cycle for each N group)
    for i, patch in enumerate(boxplot_data["boxes"]):
        color = SOLUTION_COLORS[i % num_solutions]
        patch.set_facecolor(color)
        patch.set_alpha(0.7)

    # Set up x-axis ticks to label the N groups
    # Calculate the center position for each N group
    tick_positions = [
        i * group_width + (num_solutions - 1) / 2 for i in range(num_n_groups)
    ]
    ax.set_xticks(tick_positions)
    ax.set_xticklabels(n_labels)

    for i in range(1, num_n_groups):
        # group_width = 4 (boxów) + 1.5 (przerwa) = 5.5
        # Pozycja x dla separatora: środek przerwy między grupami.
        # Wartość 0.75 to połowa odstępu 1.5.
        x_position = i * group_width - (group_width - num_solutions) / 2.0 - 0.5

        # Rysowanie pionowej linii na całej wysokości wykresu
        ax.axvline(x=x_position, color="black", linestyle="--", linewidth=1, alpha=0.6)

    # Create custom legend for the solutions (colors)
    legend_handles = [
        plt.Line2D(
            [0],
            [0],
            color=SOLUTION_COLORS[i],
            lw=4,
            label=SOLUTION_LABELS[i],
            alpha=0.7,
        )
        for i in range(num_solutions)
    ]
    ax.legend(
        legend_handles,
        SOLUTION_LABELS,
        loc="upper left",
        title="Wariant Rozwiązania",
        fontsize=9,
    )

    # Set titles and labels (using Polish as requested in the PDF context)
    ax.set_title("Czas Oczekiwania Filozofów dla wszystkich N")
    ax.set_ylabel("Średni Czas Oczekiwania [milisekundy]", fontsize=12)
    ax.set_xlabel("Liczba Filozofów (N)", fontsize=12)

    # Add grid for readability
    ax.yaxis.grid(True, linestyle="-", alpha=0.5)
    ax.xaxis.grid(False)  # Turn off vertical grid lines between boxes

    plt.tight_layout()
    plt.savefig(output_filename)
    print(f"Grouped box plot saved successfully to {output_filename}")


if __name__ == "__main__":
    print("Loading and grouping data by experiment (N)...")
    all_data_for_n, n_labels, num_n_groups = load_and_group_data(SOLUTION_FILES)

    if num_n_groups > 0:
        print(f"Successfully loaded data for {num_n_groups} experiments.")
        generate_grouped_box_plot(all_data_for_n, n_labels, num_n_groups)
    else:
        print("No data was successfully loaded to generate the grouped box plot.")
