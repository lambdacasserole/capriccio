from glob import glob
from subprocess import check_output
import re
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


def enumerate_funcs (file):
    output = check_output(['java', '-jar', 'humoresque.jar', '-e', file]).decode()
    names = filter(lambda x: x != '', output.split('\n'))
    return list(names)


def get_arity (file, func):
    output = check_output(['java', '-jar', 'humoresque.jar', '-a', func, file]).decode()
    return int(output)


def transpile_func (file, func):
    output = check_output(['java', '-jar', 'humoresque.jar', '-f', func, file]).decode()
    return output

def gen_args (arity):
    i = 0
    out = ""
    while i < arity:
        if len(out) > 0:
            out += ", "
        out += "args[" + str(i) + "]"
        i += 1
    return out

def gen_reg (names):
    out = ""
    i = 0
    while i < len(names):
        if len(out) > 0:
            out += "\n            "
        out += "evaluator.addFunction(new " + names[i] + "Function());"
        i += 1
    return out

# Go through each HAHA file in the `./src` folder.
src_files = glob('./src/*.haha')
copyfile('./build/Main.java.template', './build/Main.java')
cap_names = []
for src_file in src_files:
    func_names = enumerate_funcs(src_file) # Get functions in file.
    for func_name in func_names:
        func_capitalized_name = func_name.capitalize()
        cap_names += [func_capitalized_name]
        out_file = './build/' + func_capitalized_name + 'Function.java'
        copyfile('./build/NamedFunction.java.template', out_file)
        transpiled = transpile_func(src_file, func_name).replace('\n', '\n    ').strip()
        arity = get_arity(src_file, func_name)
        args = gen_args(arity)
        fill_template(out_file, [
            ('capitalized_name', func_capitalized_name),
            ('name', func_name),
            ('body', transpiled),
            ('arity', str(arity)),
            ('args', args)])

fill_template('./build/Main.java', [
            ('function_registration', gen_reg(cap_names))])