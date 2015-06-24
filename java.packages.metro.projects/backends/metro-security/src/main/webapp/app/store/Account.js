Ext.define('security.store.Account', {
    extend: 'Ext.data.Store',

    model: 'security.model.Account',
    proxy: {
        type: 'rest',
        url: 'accounts/findByUserId'
    }
});
