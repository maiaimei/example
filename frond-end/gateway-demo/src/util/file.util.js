function isStaticResource(path) {
    const exts = ["ico", "jpg", "jpeg", "png", "gif", "js", "css", "html"];
    const suffix = path.substr(path.lastIndexOf(".") + 1).toLowerCase();
    if (exts.indexOf(suffix) > -1) {
        return true;
    }
    return false;
}

module.exports = {
    isStaticResource
}