(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bons-de-livrison', {
            parent: 'entity',
            url: '/bons-de-livrison',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_livrison.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-livrison/bons-de-livrisons.html',
                    controller: 'Bons_de_livrisonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_livrison');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bons-de-livrison-detail', {
            parent: 'bons-de-livrison',
            url: '/bons-de-livrison/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_livrison.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-livrison/bons-de-livrison-detail.html',
                    controller: 'Bons_de_livrisonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_livrison');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bons_de_livrison', function($stateParams, Bons_de_livrison) {
                    return Bons_de_livrison.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bons-de-livrison',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bons-de-livrison-detail.edit', {
            parent: 'bons-de-livrison-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livrison/bons-de-livrison-dialog.html',
                    controller: 'Bons_de_livrisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_livrison', function(Bons_de_livrison) {
                            return Bons_de_livrison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-livrison.new', {
            parent: 'bons-de-livrison',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livrison/bons-de-livrison-dialog.html',
                    controller: 'Bons_de_livrisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date_livraison: null,
                                objet: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bons-de-livrison', null, { reload: 'bons-de-livrison' });
                }, function() {
                    $state.go('bons-de-livrison');
                });
            }]
        })
        .state('bons-de-livrison.edit', {
            parent: 'bons-de-livrison',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livrison/bons-de-livrison-dialog.html',
                    controller: 'Bons_de_livrisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_livrison', function(Bons_de_livrison) {
                            return Bons_de_livrison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-livrison', null, { reload: 'bons-de-livrison' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-livrison.delete', {
            parent: 'bons-de-livrison',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livrison/bons-de-livrison-delete-dialog.html',
                    controller: 'Bons_de_livrisonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bons_de_livrison', function(Bons_de_livrison) {
                            return Bons_de_livrison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-livrison', null, { reload: 'bons-de-livrison' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
