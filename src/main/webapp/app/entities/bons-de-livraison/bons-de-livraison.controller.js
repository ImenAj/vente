(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livraisonController', Bons_de_livraisonController);

    Bons_de_livraisonController.$inject = ['DataUtils', 'Bons_de_livraison', 'Bons_de_livraisonSearch'];

    function Bons_de_livraisonController(DataUtils, Bons_de_livraison, Bons_de_livraisonSearch) {

        var vm = this;

        vm.bons_de_livraisons = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Bons_de_livraison.query(function(result) {
                vm.bons_de_livraisons = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            Bons_de_livraisonSearch.query({query: vm.searchQuery}, function(result) {
                vm.bons_de_livraisons = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
