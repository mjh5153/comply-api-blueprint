"""Quick import test for required libraries.
This script tries to import:
 - mlxtend.frequent_patterns.apriori
 - mlxtend.frequent_patterns.association_rules
 - pandas
 - matplotlib.pyplot as plt
 - sklearn.cluster.KMeans

Run with the Python interpreter from the created virtualenv:
  source .venv/bin/activate
  python import_test.py
"""
import sys
import importlib

modules = [
    ("pandas", "pandas"),
    ("matplotlib", "matplotlib"),
    ("sklearn", "sklearn"),
    ("mlxtend", "mlxtend")
]

for name, pkg in modules:
    try:
        m = importlib.import_module(pkg)
        ver = getattr(m, "__version__", "unknown")
        print(f"Imported {name}, version={ver}")
    except Exception as e:
        print(f"ERROR importing {name}: {e}", file=sys.stderr)

# specific imports
try:
    from mlxtend.frequent_patterns import apriori, association_rules
    print("Imported apriori and association_rules from mlxtend.frequent_patterns")
except Exception as e:
    print(f"ERROR importing apriori/association_rules: {e}", file=sys.stderr)

try:
    import pandas as pd
    import matplotlib.pyplot as plt
    from sklearn.cluster import KMeans
    print("Imported pandas as pd, matplotlib.pyplot as plt, and KMeans from sklearn.cluster")
except Exception as e:
    print(f"ERROR importing pandas/matplotlib/KMeans: {e}", file=sys.stderr)

print("Import test complete.")

