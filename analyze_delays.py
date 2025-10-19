import numpy as np
import matplotlib.pyplot as plt
from pathlib import Path

def read_delay_data(file_path):
    """Read delay data from txt file"""
    delays = []
    with open(file_path, 'r') as f:
        for line in f:
            line = line.strip()
            if line and line.isdigit():
                delays.append(int(line))
    return delays

def create_box_plot(delays, title="Delay Analysis", output_file="delay_analysis.png"):
    """Create box plot"""
    plt.rcParams['font.family'] = 'DejaVu Sans Mono'
    fig, ax = plt.subplots(figsize=(6, 8))
    
    delays_array = np.array(delays)
    q1 = np.percentile(delays_array, 25)
    median = np.percentile(delays_array, 50)
    q3 = np.percentile(delays_array, 75)
    
    ax.boxplot(delays, vert=True, patch_artist=True, 
               boxprops=dict(facecolor='lightgreen', alpha=0.8, linewidth=2),
               medianprops=dict(color='red', linewidth=3),
               whiskerprops=dict(linestyle='--', color='black', linewidth=2),
               capprops=dict(color='black', linewidth=2))
    
    ax.set_ylabel('Delay (ms)', fontsize=14, fontweight='bold')
    ax.set_xlabel('Query Type', fontsize=12)
    ax.set_title(title, fontsize=16, fontweight='bold', pad=20)
    
    y_min = max(0, min(delays) - 15)
    y_max = max(delays) + 15
    ax.set_ylim(y_min, y_max)
    
    y_ticks = np.arange(y_min, y_max + 1, 5)
    ax.set_yticks(y_ticks)
    ax.set_yticklabels([f'{int(tick)}' for tick in y_ticks], fontsize=12)
    
    ax.axhline(y=q1, color='blue', linestyle=':', alpha=0.8, linewidth=2)
    ax.axhline(y=median, color='red', linestyle='-', alpha=0.8, linewidth=2)
    ax.axhline(y=q3, color='blue', linestyle=':', alpha=0.8, linewidth=2)
    
    ax.text(1.1, (q1 + min(delays)) / 2, '25% Quartile group 1', 
            ha='left', va='center', fontsize=9, fontweight='bold',
            bbox=dict(boxstyle='round,pad=0.2', facecolor='lightblue', alpha=0.7),
            transform=ax.transData)
    ax.text(1.1, (q1 + median) / 2, '25% Quartile group 2', 
            ha='left', va='center', fontsize=9, fontweight='bold',
            bbox=dict(boxstyle='round,pad=0.2', facecolor='lightgreen', alpha=0.7),
            transform=ax.transData)
    ax.text(1.1, (median + q3) / 2, '25% Quartile group 3', 
            ha='left', va='center', fontsize=9, fontweight='bold',
            bbox=dict(boxstyle='round,pad=0.2', facecolor='lightgreen', alpha=0.7),
            transform=ax.transData)
    ax.text(1.1, (q3 + max(delays)) / 2, '25% Quartile group 4', 
            ha='left', va='center', fontsize=9, fontweight='bold',
            bbox=dict(boxstyle='round,pad=0.2', facecolor='lightblue', alpha=0.7),
            transform=ax.transData)
    
    ax.set_xticklabels(['Delay Data'], fontsize=12)
    ax.grid(True, alpha=0.3)
    
    stats_text = (
        "Statistics:\n"
        f"Sample Count: {len(delays):>6d}\n"
        f"Min:          {min(delays):>6.1f} ms\n"
        f"Max:          {max(delays):>6.1f} ms\n"
        f"Mean:         {np.mean(delays):>6.1f} ms\n"
        f"Q1:           {q1:>6.1f} ms\n"
        f"Q2 (Median):  {median:>6.1f} ms\n"
        f"Q3:           {q3:>6.1f} ms"
    )
    
    ax.text(
        0.02,
        0.98,
        stats_text,
        transform=ax.transAxes,
        verticalalignment='top',
        fontsize=10,
        ha='left',
        bbox=dict(boxstyle='round,pad=0.5', facecolor='wheat', alpha=0.9),
    )
    
    plt.tight_layout()
    plt.savefig(output_file, dpi=300, bbox_inches='tight', facecolor='white')
    plt.show()
    
    return {'count': len(delays), 'min': min(delays), 'max': max(delays), 
            'mean': np.mean(delays), 'median': median, 'q1': q1, 'q3': q3}

def analyze_results_folder(results_dir="results"):
    """Analyze data files in Results folder"""
    results_path = Path(results_dir)
    data_files = list(results_path.glob('*.txt'))
    
    if not data_files:
        return
    
    for file_path in data_files:
        delays = read_delay_data(str(file_path))
        
        if not delays:
            continue
        
        output_file = f"delay_analysis_{file_path.stem}.png"
        stats = create_box_plot(delays, 
                              title=f"Delay Analysis - {file_path.stem}",
                              output_file=output_file)

def main():
    """Main function"""
    analyze_results_folder("results")

if __name__ == "__main__":
    main()
