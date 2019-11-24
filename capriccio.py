import re
import sys
import os.path
from glob import glob
from subprocess import check_output
from shutil import copyfile


def replace_in_file (path, subs):
    """ Performs a set of substitutions in a text file.
    Args:
        path (str): The path of the target file.
        subs (list of tuple): A list of pairs of terms and their replacements.
    """
    # Compile regular expressions.
    compiled_subs = []
    for old, new in subs:
        compiled_subs.append((re.compile(old), new))
    # Perform replacement.
    buffer = []
    with open(path) as file:
        for line in file:
            processed_line = line
            for old, new in compiled_subs:
                processed_line = re.sub(old, new, processed_line)
            buffer.append(processed_line)
    with open(path, 'w') as file:
        for line in buffer:
            file.write(line)


def bracket_sub (sub, comment=False):
    """ Brackets a substitution pair.
    Args:
        sub (tuple): The substitution pair to bracket.
        comment (bool): Whether or not to comment the bracketed pair.
    Returns:
        tuple: The bracketed substitution pair.
    """
    if comment:
        return ('\(\*\s*\{\{\\s*' + sub[0] + '\\s*\}\}\s*\*\)', sub[1])
    else:
        return ('\{\{\\s*' + sub[0] + '\\s*\}\}', sub[1])


def fill_template (path, subs, comment=False):
    """ Fills a template file.
    Args:
        path (str): The path of the target file.
        subs (list of tuple): The list of substitution pairs.
        comment (bool): Whether or not to replace commented tempalating expressions.
    """
    # Substitute with and without commenting.
    all_subs = [bracket_sub(sub, comment) for sub in subs]
    replace_in_file(path, all_subs)


def enumerate_funcs (path):
    """ Returns a list of function names for functions present in a HAHA file.
    Args:
        path (str): The path of the target file.
    Returns:
        list of str: The list of function names.
    """
    output = check_output(['java', '-jar', 'humoresque.jar', '-e', path]).decode()
    names = filter(lambda x: x != '', output.split('\n'))
    return list(names)


def get_arity (path, func):
    """ Returns the arity of a function present in a HAHA file.
    Args:
        path (str): The path of the target file.
    Returns:
        int: The arity.
    """
    output = check_output(['java', '-jar', 'humoresque.jar', '-a', func, path]).decode()
    return int(output)


def transpile_func (path, func):
    """ Transpiles a function present in a HAHA file to Java and returns it.
    Args:
        path (str): The path of the target file.
    Returns:
        str: The transpiled source code.
    """
    output = check_output(['java', '-jar', 'humoresque.jar', '-f', func, path]).decode()
    return output


def map_args (arity):
    """ Generates an array-argument mapping for a function with the given arity.
    Args:
        arity (int): The arity.
    Returns:
        str: The array-argument mapping.
    """
    i = 0
    out = "" # Build output here.
    while i < arity:
        if len(out) > 0:
            out += ', '
        out += f'args[{i}]' # Add mapping between array member and argument.
        i += 1
    return out


def gen_reg (class_names):
    """ Generates registration calls for funtions with the given class names.
    Args:
        class_names (list of str): The class names.
    Returns:
        str: The registration calls.
    """
    out = ""
    for class_name in class_names:
        if len(out) > 0:
            out += '\n            '
        out += f'evaluator.addFunction(new {class_name}());'
    return out


# Some filesystem contants.
SRC_DIR = './src'
BUILD_DIR = './build'
MAIN_FILE = f'{BUILD_DIR}/Main.java'

# First, check that Humoresque is available.
if not os.path.isfile('humoresque.jar'):
    print('Error: Could not find humoresque.jar', file=sys.stderr)
    exit(1)

# We'll need these for generating the main file.
class_names = []

# Go through each HAHA file in the `./src` folder.
src_files = glob(f'{SRC_DIR}/*.haha')
for src_file in src_files:
    # Go through each function in each file.
    func_names = enumerate_funcs(src_file)
    for func_name in func_names:
        func_name_capitalized = func_name.capitalize() # Capitalize name for neat, conventional Java.
        class_name = func_name_capitalized + 'Function'
        class_names += [class_name] # Remember class name,
        class_file = f'{BUILD_DIR}/' + class_name + '.java'
        # Generate class file from template.
        copyfile(f'{BUILD_DIR}/NamedFunction.java.template', class_file)
        func_body = transpile_func(src_file, func_name).replace('\n', '\n    ').strip() # Transpile HAHA to Java.
        func_arity = get_arity(src_file, func_name) # Determine function arity.
        func_args = map_args(func_arity) # Generate array-argument mapping.
        fill_template(class_file, [
            ('capitalized_name', func_name_capitalized),
            ('name', func_name),
            ('body', func_body),
            ('arity', str(func_arity)),
            ('args', func_args)])

# Generate main file and ensure functions are registered in it.
copyfile(f'{BUILD_DIR}/Main.java.template', MAIN_FILE)
fill_template(MAIN_FILE, [('function_registration', gen_reg(class_names))])
