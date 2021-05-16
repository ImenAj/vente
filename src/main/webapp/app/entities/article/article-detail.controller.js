(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Article', 'Sous_famille'];

    function ArticleDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Article, Sous_famille) {
        var vm = this;

        vm.article = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
