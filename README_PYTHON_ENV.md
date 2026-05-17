Python environment setup for this project

This repository includes a small helper to create a local Python virtual environment and install the packages used for data analysis and ML experiments.

Files added:
- `requirements.txt` — package list: pandas, matplotlib, scikit-learn, mlxtend
- `python_env_setup.sh` — script to create `.venv` and install requirements
- `import_test.py` — quick script to validate imports

Quick setup (macOS / zsh):

```bash
# create venv and install packages
./python_env_setup.sh

# activate venv
source .venv/bin/activate

# run quick import test
python import_test.py
```

If you prefer conda/Anaconda, create an environment and install the packages:

```bash
conda create -n demo-env python=3.10
conda activate demo-env
pip install -r requirements.txt
python import_test.py
```

Notes:
- If you encounter installation errors, check your Python version (Python 3.8+) and ensure pip is configured to access the network.
- On M1/M2 Macs you may need to use a conda environment or ensure wheels are available for your platform.

If you want, I can run the setup script here to create `.venv` and run the import test now.

